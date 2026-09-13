package com.imrohansoni.docleaf.features.scanner.processing

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class DocumentImageProcessor {

    fun process(bitmap: Bitmap, documentType: DocumentType): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)
        val result = when (documentType) {
            DocumentType.FILES, DocumentType.OCR -> binarize(src)     // crisp B&W scan
            DocumentType.SIGNATURE               -> binarize(src, 0.025)
            else                                 -> colorEnhance(src) // book, photo, ID
        }
        val out = createBitmap(result.cols(), result.rows())
        Utils.matToBitmap(result, out)
        return out
    }

    private fun colorEnhance(rgba: Mat): Mat {
        val bgr = Mat(); Imgproc.cvtColor(rgba, bgr, Imgproc.COLOR_RGBA2BGR)
        val lab = Mat(); Imgproc.cvtColor(bgr, lab, Imgproc.COLOR_BGR2Lab)
        val ch = ArrayList<Mat>(); Core.split(lab, ch)
        Imgproc.createCLAHE(2.0, Size(8.0, 8.0)).apply(ch[0], ch[0])  // fix lighting
        Core.merge(ch, lab)
        val outBgr = Mat(); Imgproc.cvtColor(lab, outBgr, Imgproc.COLOR_Lab2BGR)
        val outRgba = Mat(); Imgproc.cvtColor(outBgr, outRgba, Imgproc.COLOR_BGR2RGBA)
        return unsharp(outRgba)
    }

    private fun binarize(rgba: Mat, blockRatio: Double = 0.015): Mat {
        val gray = Mat(); Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGBA2GRAY)
        val enhanced = Mat(); Imgproc.createCLAHE(2.0, Size(8.0, 8.0)).apply(gray, enhanced)
        val block = (enhanced.cols() * blockRatio).toInt()
            .let { if (it % 2 == 0) it + 1 else it }.coerceIn(11, 81)
        val bin = Mat()
        Imgproc.adaptiveThreshold(enhanced, bin, 255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, block, 11.0)
        val out = Mat(); Imgproc.cvtColor(bin, out, Imgproc.COLOR_GRAY2RGBA)
        return out
    }

    private fun unsharp(mat: Mat, sigma: Double = 1.5, strength: Double = 0.4): Mat {
        val blur = Mat(); Imgproc.GaussianBlur(mat, blur, Size(0.0, 0.0), sigma)
        val out = Mat(); Core.addWeighted(mat, 1.0 + strength, blur, -strength, 0.0, out)
        return out
    }
}