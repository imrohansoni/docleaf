package com.imrohansoni.docleaf.features.scanner.camera

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.imrohansoni.docleaf.features.scanner.utils.rotate
import java.util.concurrent.atomic.AtomicBoolean

class CameraFrameAnalyzer(
    private val onFrame: (Bitmap) -> Unit,
) : ImageAnalysis.Analyzer {

    private val busy = AtomicBoolean(false)

    override fun analyze(image: ImageProxy) {
        if (!busy.compareAndSet(false, true)) { image.close(); return }
        try {
            // toBitmap() is built into CameraX 1.3+ — handles YUV conversion correctly
            val bitmap = image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())
            onFrame(bitmap)
        } finally {
            busy.set(false)
            image.close()
        }
    }
}