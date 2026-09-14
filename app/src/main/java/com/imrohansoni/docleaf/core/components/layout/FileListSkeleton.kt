package com.imrohansoni.docleaf.core.components.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState

@Composable
fun FileListSkeleton(
    modifier: Modifier = Modifier,
    rows: Int = 8,
) {
    val shimmer by animateFloatAsState(targetValue = 1f, label = "skeleton")
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        repeat(rows) {
            SkeletonRow(alpha = 0.10f * shimmer)
            Spacer(Modifier.height(12.dp))
        }
    }
}

