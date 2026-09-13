package com.imrohansoni.docleaf.core.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

private val TrackWidth = 46.dp
private val TrackHeight = 26.dp
private val ThumbSize = 20.dp
private val ThumbPadding = 3.dp

/**
 * On/off toggle switch with an animated thumb slide + track color
 * crossfade, matching Material's switch feel without depending on it.
 *
 * @param checked Current on/off state.
 * @param onCheckedChange Called with the new state when tapped. Not called
 *   when [enabled] is false.
 */
@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = LocalHapticFeedback.current

    val trackColor = when {
        !enabled && checked -> AppColors.PrimaryDisabled
        !enabled && !checked -> AppColors.SurfaceVariant
        checked -> AppColors.Primary
        else -> AppColors.Outline
    }

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) TrackWidth - ThumbSize - ThumbPadding else ThumbPadding,
        animationSpec = tween(durationMillis = 180),
        label = "switchThumbOffset"
    )

    Box(
        modifier = modifier
            .width(TrackWidth)
            .height(TrackHeight)
            .background(trackColor, RoundedCornerShape(TrackHeight))
            .semantics {
                role = Role.Switch
                toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
            ) {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onCheckedChange(!checked)
            },
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .align(Alignment.CenterStart)
                .size(ThumbSize)
                .shadow(elevation = if (enabled) 2.dp else 0.dp, shape = CircleShape)
                .background(AppColors.Surface, CircleShape)
        )
    }
}