package com.imrohansoni.docleaf.features.scanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.input.IconButton

@Composable
fun CameraTopBar(
    flashEnabled: Boolean,
    onToggleFlash: () -> Unit,
    onCameraSetting: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(
            icon = if (flashEnabled) Icons.FlashlightOff else Icons.FlashlightOn,
            contentDescription = "",
            onClick = {
                onToggleFlash()
            })

        IconButton(
            icon = Icons.Settings,
            contentDescription = "",
            onClick = {
                onCameraSetting()
            })
    }
}