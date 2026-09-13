package com.imrohansoni.docleaf.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imrohansoni.docleaf.features.scanner.model.CaptureMode

@Composable
fun SegmentedSelector(
    modifier: Modifier = Modifier,
    captureMode: CaptureMode,
    changeCaptureMode: (CaptureMode) -> Unit
) {

    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0x993B3B3B))
    ) {

        Row {
            SegmentItem(
                modifier = Modifier.wrapContentWidth(),
                title = "Automatic",
                selected = captureMode == CaptureMode.AUTO
            ) {
                changeCaptureMode(CaptureMode.AUTO)
            }

            SegmentItem(
                modifier = Modifier.wrapContentWidth(),
                title = "Manual",
                selected = captureMode == CaptureMode.MANUAL
            ) {
                changeCaptureMode(CaptureMode.MANUAL)
            }

        }

    }
}


@Composable
private fun SegmentItem(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .width(100.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (selected)
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF161616),
                            Color.Black
                        )
                    )
                else
                    SolidColor(Color.Transparent)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        BasicText(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp
            )
        )

    }
}