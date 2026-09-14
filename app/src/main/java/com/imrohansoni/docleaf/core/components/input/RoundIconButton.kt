package com.imrohansoni.docleaf.core.components.input

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.basic.Icon

@Composable
fun RoundIconButton(
    icon: IconResource,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    size: Dp = 56.dp,
    iconSize: Dp = 24.dp,
    iconTint: Color = Color.White,
    backgroundColor: Color = Color.White,
    borderColor: Color = Color.White,
    borderWidth: Dp = 1.dp,
    blurBackground: Boolean = false,
    backgroundContent: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(
            durationMillis = if (pressed) 90 else 140,
            easing = FastOutSlowInEasing,
        ),
        label = "RoundIconButtonScale",
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {

        if (blurBackground && backgroundContent != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(28.dp),
            ) {
                backgroundContent()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = backgroundColor.copy(
                        alpha = if (selected) 0.24f else 0.12f,
                    ),
                    shape = CircleShape,
                )
                .border(
                    width = borderWidth,
                    color = borderColor.copy(
                        alpha = if (selected) 0.35f else 0.18f,
                    ),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon = icon,
                contentDescription = contentDescription,
                size = iconSize,
                tint = if (enabled) {
                    iconTint
                } else {
                    iconTint.copy(alpha = 0.4f)
                },
            )
        }
    }
}