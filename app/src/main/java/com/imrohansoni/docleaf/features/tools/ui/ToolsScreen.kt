package com.imrohansoni.docleaf.features.tools.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToolsScreen(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize().background(Color.Black).padding(12.dp), contentAlignment = Alignment.Center){
        BasicText("Tools Fragment")
    }
}