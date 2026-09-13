package com.imrohansoni.docleaf.core.components


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Continuous-value slider for editing controls (brightness, saturation,
 * contrast, etc). Supports both drag and tap-to-set-position, with a
 * spring-animated thumb and a growing thumb radius while actively dragged
 * — the same kind of feedback Material's slider gives for free.
 *
 * @param value Current value, expected within [valueRange].
 * @param onValueChange Called continuously while dragging.
 * @param onValueChangeFinished Called once when the drag/tap gesture ends —
 *   useful for committing the value (e.g. re-rendering a filtered image).
 */
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    label: String = "Value",
    trackColor: Color = AppColors.SurfaceVariant,
    activeColor: Color = AppColors.Primary,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()
    val haptics = LocalHapticFeedback.current

    var trackWidthPx by remember { mutableFloatStateOf(0f) }

    val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    val thumbRadius by animateFloatAsState(
        targetValue = if (isDragged) 12f else 9f,
        animationSpec = AppMotion.PressSpring,
        label = "thumbRadius"
    )

    fun updateFromPosition(xPx: Float) {
        if (trackWidthPx <= 0f) return
        val clampedFraction = (xPx / trackWidthPx).coerceIn(0f, 1f)
        val newValue = valueRange.start + clampedFraction * (valueRange.endInclusive - valueRange.start)
        onValueChange(newValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp) // generous touch target even though the visible track is thin
            .onSizeChanged { trackWidthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        updateFromPosition(offset.x)
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onTap = { onValueChangeFinished?.invoke() }
                )
            }
            .draggable(
                orientation = Orientation.Horizontal,
                interactionSource = interactionSource,
                state = rememberDraggableState { delta ->
                    val newX = (fraction * trackWidthPx + delta).coerceIn(0f, trackWidthPx)
                    updateFromPosition(newX)
                },
                onDragStopped = { onValueChangeFinished?.invoke() },
            )
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = value,
                    range = valueRange,
                )
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        // Track background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.Center)
                .background(trackColor, RoundedCornerShape(2.dp))
        )
        // Active fill
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .height(4.dp)
                .align(Alignment.CenterStart)
                .background(activeColor, RoundedCornerShape(2.dp))
        )
        // Thumb
        val thumbOffsetPx = (fraction * trackWidthPx - thumbRadius).roundToInt()
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offsetPx(x = thumbOffsetPx, y = 0)
                .height((thumbRadius * 2).dp)
                .then(Modifier)
        ) {
            Box(
                modifier = Modifier
                    .height((thumbRadius * 2).dp)
                    .width((thumbRadius * 2).dp)
                    .background(activeColor, CircleShape)
            )
        }
    }
}

/** Small helper so we can offset by raw pixels without importing extra utils. */
private fun Modifier.offsetPx(x: Int, y: Int): Modifier = this.then(
    Modifier.layoutOffsetPx(x, y)
)

private fun Modifier.layoutOffsetPx(x: Int, y: Int): Modifier = this.then(
    Modifier.then(
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(x, y)
            }
        }
    )
)