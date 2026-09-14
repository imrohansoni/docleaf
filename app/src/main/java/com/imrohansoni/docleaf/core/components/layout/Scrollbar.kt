package com.imrohansoni.docleaf.core.components.layout

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

private val THUMB_WIDTH = 4.dp

@Composable
fun ClearFocusOnScroll(state: LazyListState, focusManager: FocusManager) {
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .collect { scrolling -> if (scrolling) focusManager.clearFocus() }
    }
}

@Composable
fun ClearFocusOnScroll(state: LazyGridState, focusManager: FocusManager) {
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .collect { scrolling -> if (scrolling) focusManager.clearFocus() }
    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun VerticalScrollbar(
    state: LazyListState,
    modifier: Modifier = Modifier,
) {
    val layout = state.layoutInfo
    Scrollbar(
        totalItems = layout.totalItemsCount,
        visibleItems = layout.visibleItemsInfo.size,
        firstVisible = state.firstVisibleItemIndex,
        active = state.isScrollInProgress,
        modifier = modifier,
    )
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun VerticalScrollbar(
    state: LazyGridState,
    modifier: Modifier = Modifier,
) {
    val layout = state.layoutInfo
    Scrollbar(
        totalItems = layout.totalItemsCount,
        visibleItems = layout.visibleItemsInfo.size,
        firstVisible = state.firstVisibleItemIndex,
        active = state.isScrollInProgress,
        modifier = modifier,
    )
}

@Composable
private fun Scrollbar(
    totalItems: Int,
    visibleItems: Int,
    firstVisible: Int,
    active: Boolean,
    modifier: Modifier = Modifier,
) {
    if (totalItems <= 0 || visibleItems <= 0 || visibleItems >= totalItems) return

    val alpha by animateFloatAsState(
        targetValue = if (active) 0.45f else 0.18f,
        label = "scrollbarAlpha",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(THUMB_WIDTH),
    ) {
        val track = maxHeight
        val fraction = (visibleItems.toFloat() / totalItems).coerceIn(0.05f, 1f)
        val thumb = track * fraction
        val scrollable = (totalItems - visibleItems).coerceAtLeast(1)
        val progress = (firstVisible.toFloat() / scrollable).coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .offset(y = (track - thumb) * progress)
                .width(THUMB_WIDTH)
                .height(thumb)
                .clip(CircleShape)
                .alpha(alpha)
                .background(Color.Gray),
        )
    }
}