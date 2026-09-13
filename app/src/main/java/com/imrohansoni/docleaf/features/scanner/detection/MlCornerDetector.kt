package com.imrohansoni.docleaf.features.scanner.detection

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.imrohansoni.docleaf.features.scanner.model.Quad
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.nio.FloatBuffer
import kotlin.math.roundToInt

/**
 * Runs an ONNX model to find the 4 corners of a document.
 *
 * HEATMAP mode      → DocAligner. Outputs 4 probability maps (one per corner).
 *                     Fast-ish, used for the LIVE preview overlay.
 * SEGMENTATION mode → u2net. Outputs a single document mask; we fit a quad to it.
 *                     Accurate on open books / low contrast, used at CAPTURE time.
 */
class MlCornerDetector(
    context: Context,
    assetFileName: String,
    private val mode: Mode,
) {
    enum class Mode { HEATMAP, SEGMENTATION }

    data class MlResult(val quad: Quad, val confidence: Float)

    companion object {
        private const val TAG = "MlCornerDetector"

        // DocAligner (HEATMAP)
        private const val DA_INPUT_SIZE = 256
        private const val DA_INPUT_NAME = "img"
        private const val HEATMAP_THRESHOLD = 0.3f
        private const val PAD_FRACTION = 0.10f

        // u2net (SEGMENTATION) — verified against your working spike.py
        private const val SEG_INPUT_SIZE = 320          // spike.py SIZE = 320
        private const val SEG_MASK_THRESHOLD = 0.5f     // spike used > 0.5
        private const val SEG_MIN_AREA_FRAC = 0.05f     // reject blobs < 5% of frame
        private val SEG_MEAN = floatArrayOf(0.485f, 0.456f, 0.406f)  // ImageNet, matches spike
        private val SEG_STD = floatArrayOf(0.229f, 0.224f, 0.225f)
    }

    private val env = OrtEnvironment.getEnvironment()
    private val session: OrtSession

    // Tensor names resolved at load time (they vary by export) instead of hardcoded.
    private val inputName: String
    private val outputName: String

    init {
        val options = OrtSession.SessionOptions().apply {
            // CPU with threads — reliable across devices (NNAPI was flaky on FastViT).
            setIntraOpNumThreads(4)
        }
        session = env.createSession(
            context.assets.open(assetFileName).use { it.readBytes() },
            options,
        )
        inputName = session.inputNames.first()
        outputName = session.outputNames.first()
        Log.d(TAG, "$assetFileName LOADED mode=$mode inputs=${session.inputNames} outputs=${session.outputNames}")
    }

    /** Single entry point — routes to the right pipeline by mode. */
    /**
     * @param usePadding only meaningful for HEATMAP. Pass true at capture time,
     *        false for live preview frames.
     */
    fun detect(bitmap: Bitmap, usePadding: Boolean = false): MlResult? = when (mode) {
        Mode.SEGMENTATION -> detectSegmentation(bitmap)
        Mode.HEATMAP -> detectDocAligner(bitmap, usePadding)
    }


    /**
     * @param usePadding true only for capture/gallery (helps corners just outside the
     *        frame). Off for live preview — padded() does full-res OpenCV work per
     *        frame and is far too slow for a 15fps loop.
     */
    private fun detectDocAligner(bitmap: Bitmap, usePadding: Boolean): MlResult? {
        val t0 = System.currentTimeMillis()

        val (workBitmap, padX, padY) =
            if (usePadding) padded(bitmap) else Triple(bitmap, 0, 0)

        val resized = workBitmap.scale(DA_INPUT_SIZE, DA_INPUT_SIZE)
        val tensorShape = longArrayOf(1, 3, DA_INPUT_SIZE.toLong(), DA_INPUT_SIZE.toLong())

        return try {
            OnnxTensor.createTensor(env, toFloatBuffer(resized), tensorShape).use { input ->
                session.run(mapOf(DA_INPUT_NAME to input)).use { output ->
                    val raw = parseHeatmapOutput(output, workBitmap.width, workBitmap.height)
                        ?: return null
                    val corners = raw.quad.toList().map { Offset(it.x - padX, it.y - padY) }
                    Log.d(TAG, "heatmap took ${System.currentTimeMillis() - t0}ms")
                    MlResult(orderCorners(corners), raw.confidence)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "docaligner inference failed", e)
            null
        }
    }

    // DocAligner preprocessing: stretch-resize, /255 only (no mean/std), NCHW.
    private fun toFloatBuffer(bmp: Bitmap): FloatBuffer {
        val pixels = IntArray(DA_INPUT_SIZE * DA_INPUT_SIZE)
        bmp.getPixels(pixels, 0, DA_INPUT_SIZE, 0, 0, DA_INPUT_SIZE, DA_INPUT_SIZE)
        val buffer = FloatBuffer.allocate(3 * DA_INPUT_SIZE * DA_INPUT_SIZE)
        for (channel in 0..2) {
            for (px in pixels) {
                val v = when (channel) {
                    0 -> (px shr 16) and 0xFF
                    1 -> (px shr 8) and 0xFF
                    else -> px and 0xFF
                }
                buffer.put(v / 255f)
            }
        }
        buffer.rewind()
        return buffer
    }


    private fun parseHeatmapOutput(out: OrtSession.Result, imgW: Int, imgH: Int): MlResult? {
        val channels = unpackChannels(out.get(outputName).get().value) ?: return null

        val corners = ArrayList<Offset>(4)
        for (channelMat in channels) {
            // Grab dimensions BEFORE releasing — reading a released Mat is a
            // use-after-free and yields garbage corners.
            val cols = channelMat.cols()
            val rows = channelMat.rows()

            val binary = Mat()
            Imgproc.threshold(channelMat, binary, HEATMAP_THRESHOLD.toDouble(), 1.0, Imgproc.THRESH_BINARY)
            val mask = Mat()
            binary.convertTo(mask, CvType.CV_8U, 255.0)

            val centroid = largestBlobCentroid(mask)

            binary.release(); mask.release(); channelMat.release()

            if (centroid == null) {
                // Release any remaining channel Mats so we don't leak on early return.
                channels.forEach { runCatching { it.release() } }
                return null
            }
            corners.add(Offset(centroid.x / cols * imgW, centroid.y / rows * imgH))
        }
        return MlResult(orderCorners(corners), 1.0f)
    }

    private fun largestBlobCentroid(mask: Mat): Offset? {
        val labels = Mat(); val stats = Mat(); val centroids = Mat()
        val count = Imgproc.connectedComponentsWithStats(mask, labels, stats, centroids)
        if (count <= 1) {
            labels.release(); stats.release(); centroids.release()
            return null
        }
        var best = -1
        var bestArea = 0
        for (i in 1 until count) {
            val area = stats.get(i, Imgproc.CC_STAT_AREA)[0].toInt()
            if (area > bestArea) { bestArea = area; best = i }
        }
        if (best < 0) {
            labels.release(); stats.release(); centroids.release()
            return null
        }
        // centroids is [count x 2] CV_64F: col 0 = x, col 1 = y (read cells separately).
        val cx = centroids.get(best, 0)[0]
        val cy = centroids.get(best, 1)[0]
        labels.release(); stats.release(); centroids.release()
        return Offset(cx.toFloat(), cy.toFloat())
    }

    private fun unpackChannels(raw: Any?): List<Mat>? {
        val batch = raw as? Array<*> ?: return null
        val channels = batch.getOrNull(0) as? Array<*> ?: return null
        return channels.mapNotNull { ch ->
            val rows = ch as? Array<*> ?: return@mapNotNull null
            val width = (rows.getOrNull(0) as? FloatArray)?.size ?: return@mapNotNull null
            val mat = Mat(rows.size, width, CvType.CV_32F)
            rows.forEachIndexed { y, row -> (row as? FloatArray)?.let { mat.put(y, 0, it) } }
            mat
        }.takeIf { it.size == 4 }
    }

    private fun padded(src: Bitmap): Triple<Bitmap, Int, Int> {
        val pad = (maxOf(src.width, src.height) * PAD_FRACTION).roundToInt()
        val mat = Mat()
        Utils.bitmapToMat(src, mat)
        val paddedMat = Mat()
        Core.copyMakeBorder(
            mat, paddedMat, pad, pad, pad, pad, Core.BORDER_CONSTANT,
            Scalar(0.0, 0.0, 0.0, 255.0)
        )
        val result = createBitmap(paddedMat.cols(), paddedMat.rows())
        Utils.matToBitmap(paddedMat, result)
        mat.release(); paddedMat.release()
        return Triple(result, pad, pad)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // u2net (SEGMENTATION)
    // ─────────────────────────────────────────────────────────────────────────
    private fun detectSegmentation(bitmap: Bitmap): MlResult? {
        val t0 = System.currentTimeMillis()

        val resized = bitmap.scale(SEG_INPUT_SIZE, SEG_INPUT_SIZE)
        val shape = longArrayOf(1, 3, SEG_INPUT_SIZE.toLong(), SEG_INPUT_SIZE.toLong())

        val mask = try {
            OnnxTensor.createTensor(env, segInputBuffer(resized), shape).use { input ->
                session.run(mapOf(inputName to input)).use { output ->
                    buildMask(output.get(outputName).get().value, bitmap.width, bitmap.height)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "segmentation inference failed", e)
            return null
        } ?: return null

        val quad = maskToQuad(mask, bitmap.width, bitmap.height)
        mask.release()

        // ⏱ CAPTURE-TIME SPEED — this number decides whether we can go
        // segmentation-only for live preview too. Watch it in Logcat.
        Log.d(TAG, "segmentation took ${System.currentTimeMillis() - t0}ms, quad=${quad != null}")
        return quad?.let { MlResult(it, 1.0f) }
    }

    // u2net preprocessing: resize to 320, /255, ImageNet normalize, NCHW.
    private fun segInputBuffer(bmp: Bitmap): FloatBuffer {
        val pixels = IntArray(SEG_INPUT_SIZE * SEG_INPUT_SIZE)
        bmp.getPixels(pixels, 0, SEG_INPUT_SIZE, 0, 0, SEG_INPUT_SIZE, SEG_INPUT_SIZE)
        val buf = FloatBuffer.allocate(3 * SEG_INPUT_SIZE * SEG_INPUT_SIZE)
        for (c in 0..2) {
            for (px in pixels) {
                val v = when (c) {
                    0 -> (px shr 16) and 0xFF
                    1 -> (px shr 8) and 0xFF
                    else -> px and 0xFF
                }
                buf.put((v / 255f - SEG_MEAN[c]) / SEG_STD[c])
            }
        }
        buf.rewind()
        return buf
    }

    // Raw saliency output → binary CV_8U mask at ORIGINAL image size.
    private fun buildMask(raw: Any?, imgW: Int, imgH: Int): Mat? {
        val batch = raw as? Array<*> ?: return null
        val channel = batch.getOrNull(0) as? Array<*> ?: return null
        val rows = channel.getOrNull(0) as? Array<*> ?: return null
        val hh = rows.size
        val ww = (rows.getOrNull(0) as? FloatArray)?.size ?: return null

        val small = Mat(hh, ww, CvType.CV_32F)
        for (y in 0 until hh) {
            (rows[y] as? FloatArray)?.let { small.put(y, 0, it) }
        }

        // Normalize 0..1 (matches spike's (m - min)/(max - min)).
        val mm = Core.minMaxLoc(small)
        val range = (mm.maxVal - mm.minVal).takeIf { it > 1e-6 } ?: 1.0
        Core.subtract(small, Scalar(mm.minVal), small)
        Core.divide(small, Scalar(range), small)

        val full = Mat()
        Imgproc.resize(small, full, Size(imgW.toDouble(), imgH.toDouble()))
        small.release()

        val mask = Mat()
        Imgproc.threshold(full, mask, SEG_MASK_THRESHOLD.toDouble(), 255.0, Imgproc.THRESH_BINARY)
        full.release()

        val mask8u = Mat()
        mask.convertTo(mask8u, CvType.CV_8U)
        mask.release()
        return mask8u
    }

    // mask → quad: epsilon sweep for a tight 4-point polygon, minAreaRect fallback.
    // (Ported from the tested Python prototype.)
    private fun maskToQuad(mask: Mat, imgW: Int, imgH: Int): Quad? {
        val contours = ArrayList<MatOfPoint>()
        Imgproc.findContours(mask, contours, Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        if (contours.isEmpty()) return null

        val largest = contours.maxByOrNull { Imgproc.contourArea(it) } ?: return null
        if (Imgproc.contourArea(largest) < SEG_MIN_AREA_FRAC * imgW * imgH) return null

        val c2f = MatOfPoint2f(*largest.toArray())
        val peri = Imgproc.arcLength(c2f, true)

        val epsFractions = doubleArrayOf(0.010, 0.015, 0.02, 0.025, 0.03, 0.035, 0.04, 0.05, 0.06, 0.08)
        for (epsFrac in epsFractions) {
            val approx = MatOfPoint2f()
            // Java order: (curve, approxCurve OUT, epsilon, closed) — not the Python order.
            Imgproc.approxPolyDP(c2f, approx, epsFrac * peri, true)
            if (approx.rows() == 4 && Imgproc.isContourConvex(MatOfPoint(*approx.toArray()))) {
                return orderCornersFromPoints(approx.toArray().map { Offset(it.x.toFloat(), it.y.toFloat()) })
            }
        }

        // Irregular/occluded blob — bounding rect always yields a quad.
        val rect = Imgproc.minAreaRect(c2f)
        val box = arrayOfNulls<Point>(4)
        rect.points(box)
        return orderCornersFromPoints(box.filterNotNull().map { Offset(it.x.toFloat(), it.y.toFloat()) })
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Shared: order 4 points TL, TR, BR, BL (index-based; Offset is a value class).
    // ─────────────────────────────────────────────────────────────────────────
    private fun orderCorners(pts: List<Offset>): Quad {
        val bySum = pts.indices.sortedBy { pts[it].x + pts[it].y }
        val tlIndex = bySum.first()
        val brIndex = bySum.last()
        val rest = pts.indices.filter { it != tlIndex && it != brIndex }
        val trIndex = rest.minByOrNull { pts[it].y - pts[it].x }!!
        val blIndex = rest.maxByOrNull { pts[it].y - pts[it].x }!!
        return Quad(
            topLeft = pts[tlIndex],
            topRight = pts[trIndex],
            bottomRight = pts[brIndex],
            bottomLeft = pts[blIndex],
        )
    }

    private fun orderCornersFromPoints(pts: List<Offset>): Quad? {
        if (pts.size < 4) return null
        return orderCorners(pts)
    }

    fun close() = session.close()
}