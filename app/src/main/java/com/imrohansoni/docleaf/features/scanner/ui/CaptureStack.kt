package com.imrohansoni.docleaf.features.scanner.ui

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CapturedStack(
    pages: List<Bitmap>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.size(58.dp)) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.10f))
                .border(1.dp, Color.White.copy(alpha = 0.30f), RoundedCornerShape(10.dp))
                .clickable(enabled = pages.isNotEmpty(), onClick = onClick),
        ) {
            pages.lastOrNull()?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Captured pages",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                )
            }
        }

        AnimatedVisibility(
            visible = pages.isNotEmpty(),
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            modifier = Modifier.align(Alignment.TopEnd).offset(x = 7.dp, y = (-7).dp),
        ) {
            Box(
                Modifier.size(23.dp).clip(CircleShape).background(Color(0xFF00E676)),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    "${pages.size}",
                    style = TextStyle(color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}