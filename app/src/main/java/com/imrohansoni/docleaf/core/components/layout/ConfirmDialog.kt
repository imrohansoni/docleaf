package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.components.input.Button
import com.imrohansoni.docleaf.core.components.input.ButtonVariant
import com.imrohansoni.docleaf.core.theme.AppTheme


@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String = "Confirm",
    cancelLabel: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.surface)
                .padding(AppTheme.dimensions.space24),
        ) {
            Text(text = title, style = AppTheme.typography.titleMedium)
            Spacer(Modifier.height(AppTheme.dimensions.space8))
            Text(
                text = message,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.onSurface.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(AppTheme.dimensions.space20))
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.space12),
            ) {
                Button(
                    variant = ButtonVariant.Outlined,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = cancelLabel)
                }
                Button(
                    onClick = { onConfirm(); onDismiss() },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = confirmLabel, color = AppTheme.colors.onPrimary)
                }
            }
        }
    }
}