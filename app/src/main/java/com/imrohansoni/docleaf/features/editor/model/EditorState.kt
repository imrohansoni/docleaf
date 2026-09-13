package com.imrohansoni.docleaf.features.editor.model

import android.graphics.Rect
import androidx.compose.runtime.Stable

@Stable
data class EditorState(
    val brightness: Float = 0f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    val rotation: Float = 0f,
    val cropRect: Rect? = null,
//    val filter: FilterState = FilterState(FilterType.NONE, 1f),
//    val drawings: List<Drawing> = emptyList(),
//    val texts: List<TextLayer> = emptyList(),
//    val watermark : Watermark? = null
)