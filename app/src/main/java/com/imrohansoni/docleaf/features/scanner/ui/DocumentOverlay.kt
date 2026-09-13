package com.imrohansoni.docleaf.features.scanner.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.features.scanner.model.DetectedDocument
import com.imrohansoni.docleaf.features.scanner.model.Edge


@Composable
fun DocumentOverlay(
    document: DetectedDocument?,
    isConfirmed: Boolean,
    isFrontCamera: Boolean,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = when {
            document == null -> 0f
            isConfirmed      -> 1f
            else             -> 0.45f   // provisional: visible but clearly "searching"
        },
        animationSpec = tween(150), label = "alpha",
    )

    val transition = rememberInfiniteTransition(label = "scan")

    val dashLength = 28f
    val gapLength = 18f

    val dashPhase by transition.animateFloat(
        initialValue = 0f, targetValue = -(dashLength + gapLength),
        animationSpec = infiniteRepeatable(tween(650, easing = LinearEasing)),
        label = "dashPhase",
    )

    val sweep by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing)),
        label = "sweep",
    )

    val green = Color(0xFF00E676)
    val mint = Color(0xFFACFFBF)

    Canvas(modifier = modifier.fillMaxSize()) {
        val doc = document ?: return@Canvas
        if (doc.imageWidth == 0 || doc.imageHeight == 0 || alpha <= 0.01f) return@Canvas

        val scale = maxOf(size.width / doc.imageWidth, size.height / doc.imageHeight)
        val dx = (size.width - doc.imageWidth * scale) / 2f
        val dy = (size.height - doc.imageHeight * scale) / 2f
        fun map(p: Offset): Offset {
            val x = if (isFrontCamera) doc.imageWidth - p.x else p.x
            return Offset(x * scale + dx, p.y * scale + dy)
        }

        val tl = map(doc.corners.topLeft); val tr = map(doc.corners.topRight)
        val br = map(doc.corners.bottomRight); val bl = map(doc.corners.bottomLeft)

        val path = Path().apply {
            moveTo(tl.x, tl.y); lineTo(tr.x, tr.y); lineTo(br.x, br.y); lineTo(bl.x, bl.y); close()
        }

        if (isConfirmed) {
            val band = Offset(size.width * (sweep * 2f - 1f), size.height * (sweep * 2f - 1f))
            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(
                        green.copy(alpha = 0.16f),   // was 0.08
                        green.copy(alpha = 0.45f),   // was 0.24 — brighter sweep band
                        green.copy(alpha = 0.16f),   // was 0.08
                    ),
                    start = band,
                    end = band + Offset(size.width, size.height),
                ),
                alpha = alpha,
            )
        } else {
            drawPath(path, green.copy(alpha = 0.20f * alpha))   // was 0.10
        }

        // Border: solid on real edges, dashed where the document is cut by the frame
        listOf(
            Triple(tl, tr, Edge.TOP), Triple(tr, br, Edge.RIGHT),
            Triple(br, bl, Edge.BOTTOM), Triple(bl, tl, Edge.LEFT),
        ).forEach { (start, end, edge) ->
            drawLine(
                color = green.copy(alpha = alpha),
                start = start, end = end,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = if (edge in doc.inferredEdges)
                    PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f) else null,
            )
        }

        // Marching DASHES around the border (replaces the dot section)
        if (isConfirmed) {
            drawPath(
                path = path,
                color = mint.copy(alpha = alpha),
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Square,
                    // Long dash + short gap = clean marching-ants look, not dots
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), dashPhase),
                ),
            )
        }
    }
}