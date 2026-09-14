package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.core.components.basic.Icon
import com.imrohansoni.docleaf.core.components.basic.Text
import com.imrohansoni.docleaf.core.theme.AppTheme
import com.imrohansoni.docleaf.core.theme.DocLeafScanner

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: IconResource? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.space24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        icon?.let {
            Icon(
                icon = it,
                size = AppTheme.dimensions.iconExtraLarge,
                tint = AppTheme.colors.onDisabled,
            )
            Spacer(Modifier.height(AppTheme.dimensions.space16))
        }
        Text(text = title, style = AppTheme.typography.titleMedium)
        Spacer(Modifier.height(AppTheme.dimensions.space8))
        Text(
            text = subtitle,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmptyStatePreview() {
    DocLeafScanner(darkTheme = true) {
        Box(Modifier.fillMaxSize().background(Color.Black)){
            EmptyState(
                "Demo title",
                subtitle = "Subtitle",
                icon = Icons.Cross
            )
        }
    }
}