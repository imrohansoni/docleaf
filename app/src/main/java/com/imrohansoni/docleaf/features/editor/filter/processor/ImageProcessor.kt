package com.imrohansoni.docleaf.features.editor.filter.processor

import android.graphics.Bitmap
import com.imrohansoni.docleaf.features.editor.filter.model.FilterType


interface ImageFilter {
    val type: FilterType
    fun process(
        bitmap: Bitmap,
        intensity: Float
    ): Bitmap
}

object MagicFilter : ImageFilter{
    override val type: FilterType = FilterType.MAGIC

    override fun process(bitmap: Bitmap, intensity: Float): Bitmap {
        TODO("Not yet implemented")
    }
}

object GrayscaleFilter : ImageFilter{
    override val type: FilterType = FilterType.GRAYSCALE

    override fun process(bitmap: Bitmap, intensity: Float): Bitmap {
        TODO("Not yet implemented")
    }
}