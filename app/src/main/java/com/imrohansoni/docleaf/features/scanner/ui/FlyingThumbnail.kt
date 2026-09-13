package com.imrohansoni.docleaf.features.scanner.ui

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

/**
 * Animates the just-captured page from the middle of the preview down into the
 * CapturedStack in the bottom-left, then calls [onFinished].
 *
 * Rendered in the ROOT Box (not inside the preview) so it isn't clipped as it
 * travels below the preview area.
 *
 * TUNE: [endXDp] / [endYDp] assume the stack sits ~40dp from the bottom-left.
 * Nudge to match your actual bottom bar.
 */
@Composable
fun FlyingThumbnail(
    bitmap: Bitmap?,
    onFinished: () -> Unit,
    endXDp: Int = 40,
    endYDp: Int = 60,
) {
    if (bitmap == null) return

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val progress = remember(bitmap) { Animatable(0f) }

        // Start: roughly the centre of the 3:4 preview (upper-middle of screen).
        val startY = with(density) { (-maxHeight * 0.18f).toPx() }
        val endX = with(density) { (-maxWidth / 2 + endXDp.dp).toPx() }
        val endY = with(density) { (maxHeight / 2 - endYDp.dp).toPx() }

        LaunchedEffect(bitmap) {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = 480, easing = FastOutSlowInEasing))
            onFinished()
        }

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .size(170.dp)
                .graphicsLayer {
                    val p = progress.value
                    translationX = lerp(0f, endX, p)
                    translationY = lerp(startY, endY, p)
                    val s = lerp(1f, 0.32f, p)
                    scaleX = s; scaleY = s
                    alpha = 1f - p * 0.30f
                }
                .clip(RoundedCornerShape(10.dp)),
        )
    }
}