package com.imrohansoni.docleaf.features.scanner.utils

import androidx.compose.ui.geometry.Offset
import com.imrohansoni.docleaf.features.scanner.model.Quad

/**
 * Pushes each corner OUTWARD from the quad's centre by [factor],
 * clamped to the image bounds. The model finds corners on a 256px
 * thumbnail so they land slightly INSIDE the true edge — expanding
 * guarantees we never cut text. (Tested: T2 grow, T3 clamp)
 */
fun Quad.expandOutward(factor: Float, imageWidth: Int, imageHeight: Int): Quad {
    val pts = toList()
    val cx = pts.map { it.x }.average().toFloat()
    val cy = pts.map { it.y }.average().toFloat()
    fun grow(p: Offset) = Offset(
        (cx + (p.x - cx) * (1f + factor)).coerceIn(0f, imageWidth.toFloat()),
        (cy + (p.y - cy) * (1f + factor)).coerceIn(0f, imageHeight.toFloat()),
    )
    return Quad(grow(topLeft), grow(topRight), grow(bottomRight), grow(bottomLeft))
}

/** Rescales a quad between two bitmap coordinate spaces. (Tested: T4) */
fun Quad.scaleTo(fromW: Int, fromH: Int, toW: Int, toH: Int): Quad {
    val sx = toW.toFloat() / fromW
    val sy = toH.toFloat() / fromH
    fun s(p: Offset) = Offset(p.x * sx, p.y * sy)
    return Quad(s(topLeft), s(topRight), s(bottomRight), s(bottomLeft))
}