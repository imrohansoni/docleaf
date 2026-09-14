package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.components.input.Button
import com.imrohansoni.docleaf.core.components.input.ButtonVariant
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner

@Composable
fun DialogBox(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Surface(
            modifier = modifier,
            shape = AppTheme.shapes.large,
            color = AppTheme.colors.surface,
            borderColor = AppTheme.colors.outline
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        AppTheme.dimensions.space24
                    ),
                verticalArrangement = Arrangement.spacedBy(
                    AppTheme.dimensions.space16
                ),
                content = content
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DialogBoxPreview() {

    DocLeafScanner(darkTheme = true) {
        Box(Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeContentPadding()) {
            DialogBox(onDismiss = {}) {
                Text(text = "Heading", style = AppTheme.typography.headingSmall)
                Text(
                    text = "This is example dialog box",
                    style = AppTheme.typography.bodySmall
                )
                Row {
                    Button(onClick = {}, variant = ButtonVariant.Outlined) {
                        Text(text = "Dismiss")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(onClick = {}) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}