package com.imrohansoni.docleaf.features.scanner.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imrohansoni.docleaf.features.scanner.model.Quad

@Composable
fun ManualCropOverlay(
    bitmap: Bitmap,
    initialQuad: Quad,
    onConfirm: (Quad) -> Unit,
    onCancel: () -> Unit,
) {
    var viewSize by remember { mutableStateOf(IntSize.Zero) }
    val points = remember { mutableStateListOf<Offset>() }   // screen-space handles
    var activeHandle by remember { mutableIntStateOf(-1) }

    val fit = remember(viewSize, bitmap) {
        if (viewSize.width == 0 || viewSize.height == 0) null
        else {
            val s = minOf(
                viewSize.width.toFloat() / bitmap.width,
                viewSize.height.toFloat() / bitmap.height,
            )
            Triple(
                s,
                (viewSize.width - bitmap.width * s) / 2f,
                (viewSize.height - bitmap.height * s) / 2f
            )
        }
    }

    LaunchedEffect(fit, initialQuad) {
        val (s, dx, dy) = fit ?: return@LaunchedEffect
        points.clear()
        points.addAll(initialQuad.toList().map { Offset(it.x * s + dx, it.y * s + dy) })
    }

    val touchRadius = with(LocalDensity.current) { 44.dp.toPx() }
    val green = Color(0xFF00E676)

    Box(Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { viewSize = it },
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(fit) {
                    val f = fit ?: return@pointerInput
                    val (s, dx, dy) = f
                    detectDragGestures(
                        onDragStart = { pos ->
                            var nearest = -1
                            var best = Float.MAX_VALUE
                            for (i in points.indices) {
                                val d = (points[i] - pos).getDistance()
                                if (d < best) {
                                    best = d; nearest = i
                                }
                            }
                            activeHandle = if (nearest != -1 && best < touchRadius) nearest else -1
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val i = activeHandle
                            if (i in points.indices) {
                                val next = points[i] + dragAmount
                                points[i] = Offset(
                                    next.x.coerceIn(dx, dx + bitmap.width * s),
                                    next.y.coerceIn(dy, dy + bitmap.height * s),
                                )
                            }
                        },
                        onDragEnd = { activeHandle = -1 },
                        onDragCancel = { activeHandle = -1 },
                    )
                },
        ) {
            if (points.size < 4) return@Canvas
            val path = Path().apply {
                moveTo(points[0].x, points[0].y); lineTo(points[1].x, points[1].y)
                lineTo(points[2].x, points[2].y); lineTo(points[3].x, points[3].y); close()
            }
            drawPath(path, green.copy(alpha = 0.15f))
            drawPath(path, green, style = Stroke(3.dp.toPx()))
            points.forEachIndexed { i, p ->
                val r = if (i == activeHandle) 16.dp.toPx() else 12.dp.toPx()
                drawCircle(Color.White, radius = r, center = p)
                drawCircle(green, radius = r, center = p, style = Stroke(3.dp.toPx()))
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable(onClick = onCancel)
                    .padding(horizontal = 28.dp, vertical = 14.dp),
            ) { BasicText("Retake", style = TextStyle(color = Color.White, fontSize = 16.sp)) }

            Box(
                Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(green)
                    .clickable(onClick = {
                        val (s, dx, dy) = fit ?: return@clickable
                        if (points.size < 4) return@clickable
                        fun back(p: Offset) = Offset(
                            ((p.x - dx) / s).coerceIn(0f, bitmap.width.toFloat()),
                            ((p.y - dy) / s).coerceIn(0f, bitmap.height.toFloat()),
                        )
                        onConfirm(
                            Quad(
                                back(points[0]),
                                back(points[1]),
                                back(points[2]),
                                back(points[3])
                            )
                        )
                    })
                    .padding(horizontal = 28.dp, vertical = 14.dp),
            ) {
                BasicText(
                    "Confirm crop",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}