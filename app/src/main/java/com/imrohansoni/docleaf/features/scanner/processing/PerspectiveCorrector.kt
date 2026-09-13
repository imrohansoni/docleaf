package com.imrohansoni.docleaf.features.scanner.processing

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.createBitmap
import com.imrohansoni.docleaf.features.scanner.model.Quad
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.pow
import kotlin.math.sqrt

/** Cuts out the document and flattens it into a straight, top-down rectangle. */
class PerspectiveCorrector {

    fun correct(bitmap: Bitmap, corners: Quad): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        val tl = corners.topLeft;     val tr = corners.topRight
        val br = corners.bottomRight; val bl = corners.bottomLeft

        // Output size = the document's real edge lengths
        val outW = maxOf(dist(bl, br), dist(tl, tr))
        val outH = maxOf(dist(tl, bl), dist(tr, br))
        if (outW < 10 || outH < 10) return bitmap   // broken quad — don't crash

        val srcPts = MatOfPoint2f(
            Point(tl.x.toDouble(), tl.y.toDouble()), Point(tr.x.toDouble(), tr.y.toDouble()),
            Point(br.x.toDouble(), br.y.toDouble()), Point(bl.x.toDouble(), bl.y.toDouble()),
        )
        val dstPts = MatOfPoint2f(
            Point(0.0, 0.0), Point(outW, 0.0), Point(outW, outH), Point(0.0, outH),
        )
        val warped = Mat()
        Imgproc.warpPerspective(src, warped, Imgproc.getPerspectiveTransform(srcPts, dstPts),
            Size(outW, outH)
        )

        val out = createBitmap(outW.toInt(), outH.toInt())
        Utils.matToBitmap(warped, out)
        return out
    }

    private fun dist(a: Offset, b: Offset) =
        sqrt((b.x - a.x).pow(2) + (b.y - a.y).pow(2)).toDouble()
}