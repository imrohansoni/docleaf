package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import com.imrohansoni.docleaf.core.theme.AppTheme


@Composable
fun Surface(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    color: Color = AppTheme.colors.surface,
    borderColor: Color = AppTheme.colors.outline,
    shape: Shape = AppTheme.shapes.large,
    role: Role? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable BoxScope.() -> Unit,
) {

    val resolvedInteractionSource =
        interactionSource ?: remember { MutableInteractionSource() }

    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) color else AppTheme.colors.disabled.copy(alpha = .12f),
        animationSpec = spring(),
        label = "surfaceColor"
    )

    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else .6f,
        animationSpec = spring(),
        label = "alpha"
    )

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            enabled = enabled,
            interactionSource = resolvedInteractionSource,
            indication = null,
            role = role,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .alpha(alpha)
            .clip(shape)
            .background(backgroundColor, shape)
            .border(
                width = AppTheme.dimensions.borderThin,
                color = borderColor,
                shape = shape
            )
            .then(clickableModifier)
            .semantics {
                if (!enabled) {
                    disabled()
                }
            },
        content = content
    )
}