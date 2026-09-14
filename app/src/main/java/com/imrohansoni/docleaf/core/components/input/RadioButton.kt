package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.imrohansoni.docleaf.core.theme.AppTheme

@Composable
fun RadioButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
) {

    val colors = AppTheme.colors
    val dimensions = AppTheme.dimensions

    val borderColor = animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabled
            isSelected -> colors.primary
            else -> colors.outline
        },
        animationSpec = spring(),
        label = "borderColor"
    )

    val dotColor = animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabled
            else -> colors.primary
        },
        animationSpec = spring(),
        label = "dotColor"
    )

    val dotScale = animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.75f,
            stiffness = 500f
        ),
        label = "dotScale"
    )

    Box(
        modifier = modifier
            .size(dimensions.checkboxSize)
            .alpha(if (enabled) 1f else .6f)
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.RadioButton,
                onClick = onClick
            )
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                if (!enabled) {
                    disabled()
                }
                contentDescription?.let {
                    this.contentDescription = it
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val stroke = dimensions.borderMedium.toPx()
            drawCircle(
                color = borderColor.value,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )
            if (dotScale.value > 0f) {
                drawCircle(
                    color = dotColor.value,
                    radius = size.minDimension * .25f * dotScale.value
                )
            }
        }
    }
}