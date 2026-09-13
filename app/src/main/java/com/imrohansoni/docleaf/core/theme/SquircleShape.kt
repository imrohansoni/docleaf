package com.imrohansoni.docleaf.core.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.min

class SquircleShape(
    private val curveFactor: Float = 0.45f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val w = size.width
        val h = size.height
        val c = min(w, h) * curveFactor

        val path = Path().apply {
            moveTo(c, 0f)
            lineTo(w - c, 0f)
            cubicTo(w, 0f, w, 0f, w, c)

            lineTo(w, h - c)
            cubicTo(w, h, w, h, w - c, h)

            lineTo(c, h)
            cubicTo(0f, h, 0f, h, 0f, h - c)

            lineTo(0f, c)
            cubicTo(0f, 0f, 0f, 0f, c, 0f)

            close()
        }

        return Outline.Generic(path)
    }
}