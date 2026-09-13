package com.imrohansoni.docleaf.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * Drop-in replacement for Material's ripple indication. Combines a subtle
 * scale-down with a soft color overlay on press, both spring-animated.
 *
 * Pair with `clickable(interactionSource = source, indication = null)` so
 * you don't get a double effect from the platform ripple.
 */
fun Modifier.pressFeedback(
    interactionSource: InteractionSource,
    pressedScale: Float = AppMotion.PressedScaleDefault,
    overlayColor: Color = Color.Black.copy(alpha = 0.08f),
): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = AppMotion.PressSpring,
        label = "pressScale"
    )
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = AppMotion.PressSpring,
        label = "pressOverlay"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .drawWithContent {
            drawContent()
            if (overlayAlpha > 0f) {
                drawRect(color = overlayColor.copy(alpha = overlayColor.alpha * overlayAlpha))
            }
        }
}

/**
 * Fires a light click haptic. Call from onClick lambdas so every
 * interactive component feels consistent — this is what Material gives
 * you for free and is easy to forget when rolling your own.
 */
@Composable
fun rememberClickHaptic(): () -> Unit {
    val haptics = LocalHapticFeedback.current
    return { haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove) }
}