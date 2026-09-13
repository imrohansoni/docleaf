package com.imrohansoni.docleaf.features.scanner.camera

import androidx.camera.core.AspectRatio
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector

/**
 * Single source of truth for camera geometry.
 *
 * CameraX names ratios in SENSOR orientation, so RATIO_4_3 renders as 3:4 in
 * portrait — which matches Modifier.aspectRatio(3f / 4f) on the preview Box.
 *
 * All three use cases (Preview, ImageAnalysis, ImageCapture) MUST share this.
 * If they drift apart, the live overlay quad and the captured photo are framed
 * differently, and the crop lands in the wrong place.
 */
object CameraConfig {

    private val portrait3x4 = AspectRatioStrategy(
        AspectRatio.RATIO_4_3,
        AspectRatioStrategy.FALLBACK_RULE_AUTO,
    )

    /** Used for Preview, ImageAnalysis and ImageCapture alike. */
    fun selector(): ResolutionSelector =
        ResolutionSelector.Builder()
            .setAspectRatioStrategy(portrait3x4)
            .build()
}