package com.imrohansoni.docleaf.features.scanner.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.imrohansoni.docleaf.core.components.Icon
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import kotlin.math.absoluteValue

@Composable
fun DocumentTypeSlider(
    modifier: Modifier = Modifier,
    currentDocumentType: DocumentType,
    onClick: (DocumentType) -> Unit
) {

    val types = DocumentType.entries

    val pagerState = rememberPagerState(
        initialPage = types.indexOf(currentDocumentType)
    ) {
        types.size
    }

    LaunchedEffect(pagerState.currentPage) {
        onClick(types[pagerState.currentPage])
    }

    val itemWidth = 120.dp

    val contentPadding = PaddingValues(
        horizontal = LocalConfiguration.current.screenWidthDp.dp / 2 - itemWidth / 2
    )

    HorizontalPager(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        state = pagerState,
        pageSize = PageSize.Fixed(itemWidth),
        contentPadding = contentPadding,
        pageSpacing = 12.dp
    ) { page ->

        val type = types[page]

        val pageOffset = (
                (pagerState.currentPage - page) +
                        pagerState.currentPageOffsetFraction
                ).absoluteValue

        val scale by animateFloatAsState(
            targetValue = lerp(
                start = 0.85f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            ),
            label = ""
        )

        val alpha by animateFloatAsState(
            targetValue = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            ),
            label = ""
        )

        Box(
            modifier = Modifier
                .width(itemWidth)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    icon = type.icon,
                    color = if (page == pagerState.currentPage) Color.White else Color.Gray,
                    size = if (page == pagerState.currentPage) 24.dp else 32.dp
                )

                Spacer(Modifier.width(8.dp))

                BasicText(
                    text = type.title,
                    style = TextStyle(
                        color =
                            if (page == pagerState.currentPage)
                                Color.White
                            else
                                Color.Gray,
                        fontSize =
                            if (page == pagerState.currentPage)
                                18.sp
                            else
                                14.sp,

                        )
                )
            }

        }
    }
}