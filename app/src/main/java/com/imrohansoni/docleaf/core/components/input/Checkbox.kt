package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.theme.AppTheme

@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "checkboxScale"
    )

    val background by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colors.disabled
            checked -> AppTheme.colors.primary
            else -> Color.Transparent
        },
        label = "checkboxBackground"
    )

    val border by animateColorAsState(
        targetValue = when {
            !enabled -> AppTheme.colors.disabled
            checked -> AppTheme.colors.primary
            else -> AppTheme.colors.outline
        },
        label = "checkboxBorder"
    )

    Box(
        modifier = modifier
            .size(AppTheme.dimensions.checkboxSize)
            .scale(scale)
            .clip(AppTheme.shapes.small)
            .background(background)
            .border(
                AppTheme.dimensions.borderThin,
                border,
                AppTheme.shapes.small
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = checked,
            enter = scaleIn(),
            exit = scaleOut()
        ) {

            Icon(
                icon = Icons.Check,
                size = AppTheme.dimensions.iconExtraSmall,
                tint = AppTheme.colors.onPrimary,
                contentDescription = null
            )
        }
    }
}