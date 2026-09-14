package com.imrohansoni.docleaf.core.components.layout


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Stable
class StaggerState internal constructor() {
    private val played = mutableSetOf<Any>()
    internal fun claim(key: Any): Boolean = played.add(key)
    internal fun hasPlayed(key: Any): Boolean = key in played
}

@Composable
fun rememberStaggerState(): StaggerState = remember { StaggerState() }

@Composable
fun Modifier.staggeredEntry(
    state: StaggerState,
    key: Any,
    index: Int,
    stepMs: Int = 28,
    durationMs: Int = 260,
    maxStaggered: Int = 12,
    slideFrom: Dp = 16.dp,
): Modifier {
    val isFirstAppearance = remember(key) { state.claim(key) }
    if (!isFirstAppearance) return this

    var shown by remember(key) { mutableStateOf(false) }
    LaunchedEffect(key) {
        delay((index.coerceAtMost(maxStaggered) * stepMs).toLong())
        shown = true
    }

    val progress by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(durationMs, easing = LinearOutSlowInEasing),
        label = "staggerEntry",
    )

    val slidePx = with(LocalDensity.current) { slideFrom.toPx() }
    return this
        .alpha(progress)
        .graphicsLayer { translationY = (1f - progress) * slidePx }
}