package com.imrohansoni.docleaf.features.scanner.model

import android.graphics.Bitmap

sealed class CaptureState {
    object Idle : CaptureState()
    object Processing : CaptureState()
    /** Photo taken (or gallery image loaded) — user reviews/adjusts the crop. */
    data class AwaitingCorrection(val rawBitmap: Bitmap, val detectedQuad: Quad) : CaptureState()
    data class Error(val message: String) : CaptureState()
}