package com.imrohansoni.docleaf.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.core.theme.PdfAppTheme


@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    color : Color = Color.White,
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
        Icon(icon = icon, color = color, size = 22.dp)
    }
}

@Preview
@Composable
private fun IconButtonPreview() {
    PdfAppTheme {
        IconButton(icon = Icons.Mail) { }
    }
}

