package com.imrohansoni.docleaf.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FlipCameraButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier
            .size(50.dp)
            .background(Color(0xFF252525), CircleShape)
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon = Icons.Flip, color = Color.White, size = 28.dp)
    }
}


@Composable
fun FilePickerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier
            .size(50.dp)
            .background(Color(0xFF252525), CircleShape)
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {

    }
}