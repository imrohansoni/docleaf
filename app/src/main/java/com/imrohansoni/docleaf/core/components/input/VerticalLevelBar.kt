package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme


@Composable
fun VerticalLevelBar(
    fraction: Float,
    modifier: Modifier = Modifier,
    label: String? = null,
    icon: String? = null,
    barWidth: Dp = 6.dp,
    barHeight: Dp = 140.dp,
    showPercent: Boolean = true,
) {
    val clamped = fraction.coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue = clamped,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh, // snappy: it tracks a finger
        ),
        label = "levelFill",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(AppTheme.shapes.medium)
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(
                horizontal = AppTheme.dimensions.space12,
                vertical = AppTheme.dimensions.space16,
            ),
    ) {
        if (icon != null) {
            Text(text = icon, style = AppTheme.typography.titleMedium)
            Spacer(Modifier.height(AppTheme.dimensions.space8))
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .width(barWidth)
                .height(barHeight)
                .clip(RoundedCornerShape(percent = 50))
                .background(Color.White.copy(alpha = 0.25f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animated)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(AppTheme.colors.primary),
            )
        }

        if (showPercent) {
            Spacer(Modifier.height(AppTheme.dimensions.space8))
            Text(
                text = "${(clamped * 100).toInt()}%",
                style = AppTheme.typography.caption,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(40.dp)
            )
        }
        if (label != null) {
            Text(
                text = label,
                style = AppTheme.typography.caption,
                color = Color.White.copy(alpha = 0.7f),
            )
        }
    }
}