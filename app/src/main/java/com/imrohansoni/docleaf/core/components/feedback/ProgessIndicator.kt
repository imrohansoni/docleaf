package com.imrohansoni.docleaf.core.components.feedback

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.imrohansoni.docleaf.core.theme.AppTheme

@Composable
fun ProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = AppTheme.dimensions.loadingMedium,
    color: Color = AppTheme.colors.primary,
    trackColor: Color = AppTheme.colors.outline,
    strokeWidth: Dp = AppTheme.dimensions.borderMedium,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 350,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )

    Canvas(
        modifier = modifier
            .size(size)
            .semantics {
//               role = Role.ProgressBar
                progressBarRangeInfo =
                    ProgressBarRangeInfo(animatedProgress, 0f..1f)
            }
    ) {

        // Track
        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )

        // Progress
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}