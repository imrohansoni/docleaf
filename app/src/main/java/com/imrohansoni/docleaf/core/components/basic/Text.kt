package com.imrohansoni.docleaf.core.components.basic


import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.imrohansoni.docleaf.core.theme.AppTheme

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.typography.bodyMedium,
    color: Color = AppTheme.colors.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign = TextAlign.Start,
) {

    BasicText(
        text = text,
        modifier = modifier.semantics(mergeDescendants = true) {},
        maxLines = maxLines,
        overflow = overflow,
        style = style.copy(
            color = color,
            textAlign = textAlign
        )
    )
}

