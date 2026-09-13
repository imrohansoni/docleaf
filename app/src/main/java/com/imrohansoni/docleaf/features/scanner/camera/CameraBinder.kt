package com.imrohansoni.docleaf.features.scanner.camera

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

object CameraBinder {
    fun bind(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        imageCapture: ImageCapture,
        imageAnalysis: ImageAnalysis,
        cameraSelector: CameraSelector,
        onBound: (Camera) -> Unit = {},
    ) {
        val future = ProcessCameraProvider.getInstance(context)

        future.addListener({
            val provider = future.get()
            val preview = Preview.Builder()
                .setResolutionSelector(CameraConfig.selector())
                .build()
                .also { it.surfaceProvider = previewView.surfaceProvider }

            provider.unbindAll()

            val camera = provider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis,
            )

            onBound(camera)
        }, ContextCompat.getMainExecutor(context))
    }
}