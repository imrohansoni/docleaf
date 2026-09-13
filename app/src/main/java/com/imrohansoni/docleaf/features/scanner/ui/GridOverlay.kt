package com.imrohansoni.docleaf.features.scanner.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.imrohansoni.docleaf.features.scanner.strategy.CaptureGuide
//import com.imrohansoni.docleaf.features.scanner.strategy.CaptureHint

/** Static per-strategy guide (oval / box / split line) + coaching hint. */
//@Composable
//fun GuideOverlay(
//    guide: CaptureGuide,
//    hint: CaptureHint?,
//    modifier: Modifier = Modifier,
//) {
//    val green = Color(0xFF00E676)
//
//    Box(modifier) {
//        Canvas(Modifier.fillMaxSize()) {
//            val dash = PathEffect.dashPathEffect(floatArrayOf(24f, 16f))
//            when (guide) {
//                is CaptureGuide.Oval -> {
//                    val w = size.width * guide.widthFraction
//                    val h = size.height * guide.heightFraction
//                    drawOval(
//                        color = green,
//                        topLeft = Offset((size.width - w) / 2f, (size.height - h) / 2f),
//                        size = Size(w, h),
//                        style = Stroke(3.dp.toPx(), pathEffect = dash),
//                    )
//                }
//                is CaptureGuide.Rect -> {
//                    val w = size.width * guide.widthFraction
//                    val h = w / guide.aspect
//                    drawRoundRect(
//                        color = green,
//                        topLeft = Offset((size.width - w) / 2f, (size.height - h) / 2f),
//                        size = Size(w, h),
//                        cornerRadius = CornerRadius(12.dp.toPx()),
//                        style = Stroke(3.dp.toPx()),
//                    )
//                }
//                CaptureGuide.SplitLine -> drawLine(
//                    color = green.copy(alpha = 0.55f),
//                    start = Offset(size.width / 2f, 0f),
//                    end = Offset(size.width / 2f, size.height),
//                    strokeWidth = 2.dp.toPx(),
//                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 14f)),
//                )
//                CaptureGuide.None -> Unit
//            }
//        }
//
//        AnimatedVisibility(
//            visible = hint != null,
//            enter = fadeIn(), exit = fadeOut(),
//            modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp),
//        ) {
//            hint?.let {
//                BasicText(
//                    text = it.message,
//                    style = TextStyle(color = Color.White, fontSize = 14.sp),
//                    modifier = Modifier
//                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
//                        .padding(horizontal = 16.dp, vertical = 8.dp),
//                )
//            }
//        }
//    }
//}