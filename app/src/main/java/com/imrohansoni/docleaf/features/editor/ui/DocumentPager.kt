package com.imrohansoni.docleaf.features.editor.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.imrohansoni.docleaf.features.editor.DocumentPagerState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DocumentPager(
    pageCount: Int,
    state: DocumentPagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {

    state.pageCount = pageCount

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val offsetX = remember {
            Animatable(0f)
        }

        val coroutine = rememberCoroutineScope()

        Box(Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        offsetX.value.roundToInt(),
                        0
                    )
                }
                .pointerInput(state.currentPage) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            coroutine.launch {
                                offsetX.snapTo(
                                    offsetX.value + dragAmount
                                )
                            }
                        },
                        onDragEnd = {
                            coroutine.launch {
                                when {
                                    offsetX.value < -width / 4 -> {
                                        state.next()
                                    }

                                    offsetX.value > width / 4 -> {
                                        state.previous()
                                    }
                                }
                                offsetX.animateTo(
                                    0f,
                                    tween(250)
                                )
                            }
                        }
                    )
                }
        ) {
            content(state.currentPage)
        }
    }
}