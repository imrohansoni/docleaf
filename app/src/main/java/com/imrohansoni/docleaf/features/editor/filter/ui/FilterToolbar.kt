package com.imrohansoni.docleaf.features.editor.filter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterToolbar(modifier: Modifier = Modifier) {
    Box(Modifier
        .fillMaxWidth()
        .height(50.dp)) {
        BasicText(text = "Filter Toolbar")
    }
}