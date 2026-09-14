package com.imrohansoni.docleaf.core.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner

data class SeekBarColors(
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val thumbColor: Color,
    val disabledTrackColor: Color,
    val disabledThumbColor: Color,
)

@Stable
object SeekBarDefaults {
    @Composable
    @ReadOnlyComposable
    fun colors(enabled: Boolean): SeekBarColors {
        val colors = AppTheme.colors
        return SeekBarColors(
            activeTrackColor = colors.primary,
            inactiveTrackColor = colors.outline,
            thumbColor = colors.primary,
            disabledTrackColor = colors.disabled,
            disabledThumbColor = colors.disabled,
        )
    }
    @Composable @ReadOnlyComposable fun trackHeight(): Dp = 6.dp
    @Composable @ReadOnlyComposable fun thumbSize(): Dp = 12.dp
    @Composable @ReadOnlyComposable fun touchHeight(): Dp = 40.dp
    @Composable @ReadOnlyComposable fun thumbSizeDragged(): Dp = 16.dp
}

@Composable
fun SeekBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    require(valueRange.endInclusive > valueRange.start) {
        "Invalid valueRange"
    }

    val colors = SeekBarDefaults.colors(enabled)

    val trackHeight = SeekBarDefaults.trackHeight()
    val thumbSize = SeekBarDefaults.thumbSize()
    val thumbSizeDragged = SeekBarDefaults.thumbSizeDragged()
    val touchHeight = SeekBarDefaults.touchHeight()

    val span = valueRange.endInclusive - valueRange.start

    var trackWidth by remember {
        mutableFloatStateOf(0f)
    }

    var dragFraction by remember {
        mutableStateOf<Float?>(null)
    }

    val targetFraction =
        ((value - valueRange.start) / span)
            .coerceIn(0f, 1f)

    val progressFraction = dragFraction ?: targetFraction

    val currentThumb =
        if (dragFraction != null)
            thumbSizeDragged
        else
            thumbSize

    var lastReportedFraction by remember {
        mutableFloatStateOf(Float.NaN)
    }

    fun report(fraction: Float) {
        if (fraction == lastReportedFraction) return

        lastReportedFraction = fraction

        onValueChange(
            valueRange.start + fraction * span
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(touchHeight)
            .onSizeChanged {
                trackWidth = it.width.toFloat()
            }
            .pointerInput(enabled) {

                if (!enabled) return@pointerInput

                detectTapGestures { offset ->

                    if (trackWidth <= 0f) return@detectTapGestures

                    val fraction =
                        (offset.x / trackWidth)
                            .coerceIn(0f, 1f)

                    report(fraction)

                    lastReportedFraction = Float.NaN

                    onValueChangeFinished?.invoke()
                }
            }
            .pointerInput(enabled) {

                if (!enabled) return@pointerInput

                awaitPointerEventScope {

                    while (true) {

                        val down =
                            awaitFirstDown(requireUnconsumed = false)

                        down.consume()

                        if (trackWidth <= 0f) continue

                        val startFraction =
                            (down.position.x / trackWidth)
                                .coerceIn(0f, 1f)

                        dragFraction = startFraction
                        report(startFraction)

                        while (true) {

                            val event = awaitPointerEvent()

                            val change =
                                event.changes.firstOrNull()
                                    ?: break

                            if (change.changedToUpIgnoreConsumed()) {
                                dragFraction = null
                                lastReportedFraction = Float.NaN
                                onValueChangeFinished?.invoke()
                                break
                            }

                            if (change.isConsumed) {
                                dragFraction = null
                                lastReportedFraction = Float.NaN
                                break
                            }

                            val fraction =
                                (change.position.x / trackWidth)
                                    .coerceIn(0f, 1f)

                            dragFraction = fraction
                            report(fraction)

                            change.consume()
                        }
                    }
                }
            }
            .semantics {
                role = Role.Button
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = value,
                    range = valueRange,
                    steps = 0,
                )
            },
        contentAlignment = Alignment.CenterStart,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(CircleShape)
                .background(
                    if (enabled)
                        colors.inactiveTrackColor
                    else
                        colors.disabledTrackColor,
                ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(progressFraction)
                .height(trackHeight)
                .clip(CircleShape)
                .background(
                    if (enabled)
                        colors.activeTrackColor
                    else
                        colors.disabledTrackColor,
                ),
        )

//        Box(
//            modifier = Modifier
//                .offset {
//                    IntOffset(
//                        x = ((trackWidth - currentThumb.toPx()) * progressFraction).toInt(),
//                        y = 0,
//                    )
//                }
//                .size(currentThumb)
//                .clip(CircleShape)
//                .background(
//                    if (enabled)
//                        colors.thumbColor
//                    else
//                        colors.disabledThumbColor,
//                ),
//        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SeekBarPreview() {

    var value by remember {
        mutableFloatStateOf(.35f)
    }

    DocLeafScanner {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            SeekBar(
                value = value,
                onValueChange = {
                    value = it
                }
            )
        }
    }
}