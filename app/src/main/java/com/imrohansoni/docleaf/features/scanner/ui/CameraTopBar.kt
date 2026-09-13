package com.imrohansoni.docleaf.features.scanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.components.FlashlightButton
import com.imrohansoni.docleaf.core.components.IconButton
import com.imrohansoni.docleaf.core.components.Icons

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

        FlashlightButton(flashlightOn = flashEnabled) {
            onToggleFlash()
        }

        IconButton(icon = Icons.Settings) {
            onCameraSetting()
        }
    }
}