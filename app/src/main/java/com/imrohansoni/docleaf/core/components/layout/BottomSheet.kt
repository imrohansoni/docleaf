package com.imrohansoni.docleaf.core.components.layout

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.components.input.Button
import com.imrohansoni.docleaf.core.components.input.SeekBar
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner
import kotlin.math.roundToInt

@Composable
fun BottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (visible) BackHandler(onBack = onDismiss)

    var sheetHeightPx by remember { mutableFloatStateOf(0f) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    LaunchedEffect(visible) { if (visible) dragOffset = 0f }

    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        label = "sheetOffset",
    )
    val yOffset = if (dragging) dragOffset else animatedOffset

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
            val scrimAlpha = if (sheetHeightPx > 0f) {
                (0.5f * (1f - (yOffset / sheetHeightPx))).coerceIn(0f, 0.5f)
            } else 0.5f
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = scrimAlpha))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    ),
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { sheetHeightPx = it.height.toFloat() }
                    .offset { IntOffset(0, yOffset.roundToInt()) }
                    .clip(AppTheme.shapes.extraLarge)
                    .background(AppTheme.colors.surface)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    )
                    .pointerInput(sheetHeightPx) {
                        detectVerticalDragGestures(
                            onDragStart = { dragging = true },
                            onVerticalDrag = { _, dragAmount ->
                                dragOffset = (dragOffset + dragAmount).coerceAtLeast(0f)
                            },
                            onDragEnd = {
                                dragging = false
                                val threshold = sheetHeightPx * 0.35f
                                if (dragOffset > threshold) onDismiss() else dragOffset = 0f
                            },
                            onDragCancel = {
                                dragging = false
                                dragOffset = 0f
                            },
                        )
                    }
                    .navigationBarsPadding()
                    .padding(AppTheme.dimensions.space20),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(36.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.outline),
                )
                Spacer(Modifier.height(AppTheme.dimensions.space16))
                content()
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetPreview() {
    var showSheet by remember {
        mutableStateOf(false)
    }

    var brightness by remember { mutableFloatStateOf(0f) }
    var contrast by remember { mutableFloatStateOf(0f) }
    var saturation by remember { mutableFloatStateOf(0f) }
    var sharpness by remember { mutableFloatStateOf(0f) }

    DocLeafScanner(darkTheme = true) {
        Box(Modifier.fillMaxSize().background(Color.Gray)){
            Box(
                Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        showSheet = true
                    }
                ) {
                    Text("Show Sheet")
                }
                if (showSheet) {
                    BottomSheet(
                        onDismiss = {
                            showSheet = false
                        },
                        visible = showSheet,
                    ) {
                        Text(
                            text = "Scanner Settings",
                            style = AppTheme.typography.headingMedium
                        )
                        Spacer(
                            Modifier.height(
                                AppTheme.dimensions.space16
                            )
                        )

                        Text("Brightness")
                        SeekBar(
                            value = brightness,
                            onValueChange = {
                                brightness = it
                            }
                        )

                        Text("Sharpness")
                        SeekBar(
                            value = sharpness,
                            onValueChange = {
                                sharpness = it
                            }
                        )


                        Text("Saturation")
                        SeekBar(
                            value = saturation,
                            onValueChange = {
                                saturation = it
                            }
                        )

                        Text("Contrast")
                        SeekBar(
                            value = contrast,
                            onValueChange = {
                                contrast = it
                            }
                        )
                    }
                }
            }
        }
    }
}