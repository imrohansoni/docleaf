package com.imrohansoni.docleaf.features.scanner.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imrohansoni.docleaf.features.scanner.model.ShutterState

@Composable
fun CaptureButton(
    state: ShutterState,
    onCapture: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 84.dp,
    ringStroke: androidx.compose.ui.unit.Dp = 4.dp,
    innerSize: androidx.compose.ui.unit.Dp = 62.dp,
) {
    val green = Color(0xFF00E676)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    // Ring fill: 0f idle → 1f when the countdown completes.
    val ringProgress by animateFloatAsState(
        targetValue = when (state) {
            is ShutterState.Countdown -> state.progress
            ShutterState.Capturing -> 1f
            ShutterState.Idle -> 0f
        },
        // Short tween keeps the arc smooth without lagging the 50ms tick.
        animationSpec = tween(80, easing = LinearEasing),
        label = "ringProgress",
    )

    // Tactile press feedback + a small "settle" while capturing.
    val innerScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.88f
            state is ShutterState.Capturing -> 0.82f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "innerScale",
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = null,                                   // ring is the feedback
                enabled = state !is ShutterState.Capturing,
                onClick = onCapture,
            ),
        contentAlignment = Alignment.Center,
    ) {
        // ── Outer ring: track + countdown arc ────────────────────────────────
        Canvas(Modifier.fillMaxSize()) {
            val stroke = ringStroke.toPx()
            val inset = stroke / 2f
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)

            drawCircle(
                color = Color.White.copy(alpha = 0.28f),
                radius = (this.size.minDimension - stroke) / 2f,
                style = Stroke(stroke),
            )

            if (ringProgress > 0.001f) {
                drawArc(
                    color = green,
                    startAngle = -90f,                 // start at 12 o'clock
                    sweepAngle = 360f * ringProgress,  // clockwise
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = Stroke(stroke, cap = StrokeCap.Round),
                )
            }
        }

        // ── Inner circle: countdown number / spinner / plain white ───────────
        Box(
            modifier = Modifier
                .size(innerSize)
                .scale(innerScale)
                .clip(CircleShape)
                .background(
                    when (state) {
                        ShutterState.Capturing -> Color.White.copy(alpha = 0.55f)
                        else -> Color.White
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is ShutterState.Countdown -> {
                    // Number pops slightly each time it ticks over.
                    val pop by animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "pop-${state.secondsLeft}",
                    )
                    BasicText(
                        text = "${state.secondsLeft}",
                        style = TextStyle(
                            color = Color(0xFF0A0A0A),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.scale(pop),
                    )
                }

                ShutterState.Capturing -> {
                    val rotation by rememberInfiniteTransition(label = "spin")
                        .animateFloat(
                            initialValue = 0f, targetValue = 360f,
                            animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing)),
                            label = "rot",
                        )
                    Canvas(Modifier.size(26.dp)) {
                        drawArc(
                            color = Color(0xFF0A0A0A),
                            startAngle = rotation,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(3.dp.toPx(), cap = StrokeCap.Round),
                        )
                    }
                }

                ShutterState.Idle -> Unit   // plain white circle
            }
        }
    }
}