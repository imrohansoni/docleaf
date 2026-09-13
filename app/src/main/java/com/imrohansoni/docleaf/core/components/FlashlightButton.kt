package com.imrohansoni.docleaf.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FlashlightButton(
    modifier: Modifier = Modifier,
    flashlightOn: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier
            .size(36.dp)
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon = if (flashlightOn) Icons.FlashlightOn else Icons.FlashlightOff, size = 22.dp)
    }
}