package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.theme.AppTheme

@Composable
fun IconButton(
    icon: IconResource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize : Dp = AppTheme.dimensions.iconMedium,
    tint: Color = AppTheme.colors.onSurface,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "IconButtonScale"
    )

    val iconTint = if (enabled) {
        tint
    } else {
        AppTheme.colors.onDisabled
    }

    Box(
        modifier = modifier
            .size(iconSize + 16.dp)
            .scale(scale)
            .clip(AppTheme.shapes.medium)
            .background(Color.Transparent)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .semantics {
                role = Role.Button
                if (!enabled) disabled()
            },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            icon = icon,
            size = iconSize,
            tint = iconTint,
            contentDescription = contentDescription
        )
    }
}