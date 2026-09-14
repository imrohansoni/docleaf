package com.imrohansoni.docleaf.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Immutable
class SquircleShape(
    private val radius: Dp = 16.dp,
    private val smoothness: Float = SquircleDefaults.Smoothness
) : Shape {

    init {
        require(smoothness in 0f..1f) {
            "Smoothness must be between 0f and 1f."
        }
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        if (size.width == 0f || size.height == 0f) {
            return Outline.Generic(Path())
        }

        val cornerRadius = with(density) {
            radius.toPx()
        }.coerceAtMost(min(size.width, size.height) / 2f)

        val control = cornerRadius * smoothness

        val w = size.width
        val h = size.height

        val path = Path().apply {

            moveTo(cornerRadius, 0f)

            // Top edge
            lineTo(w - cornerRadius, 0f)
            cubicTo(
                w - cornerRadius + control,
                0f,
                w,
                cornerRadius - control,
                w,
                cornerRadius
            )

            // Right edge
            lineTo(w, h - cornerRadius)
            cubicTo(
                w,
                h - cornerRadius + control,
                w - cornerRadius + control,
                h,
                w - cornerRadius,
                h
            )

            // Bottom edge
            lineTo(cornerRadius, h)
            cubicTo(
                cornerRadius - control,
                h,
                0f,
                h - cornerRadius + control,
                0f,
                h - cornerRadius
            )

            // Left edge
            lineTo(0f, cornerRadius)
            cubicTo(
                0f,
                cornerRadius - control,
                cornerRadius - control,
                0f,
                cornerRadius,
                0f
            )

            close()
        }

        return Outline.Generic(path)
    }
}

object SquircleDefaults {
    const val Smoothness = 0.82f
}