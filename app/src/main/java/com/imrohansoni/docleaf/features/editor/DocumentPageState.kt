package com.imrohansoni.docleaf.features.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class DocumentPagerState(
    initialPage: Int = 0
) {

    var currentPage by mutableIntStateOf(initialPage)
        internal set

    internal var pageCount by mutableIntStateOf(0)

    suspend fun animateToPage(page: Int) {
        currentPage = page.coerceIn(0, pageCount - 1)
    }

    fun next() {
        currentPage = (currentPage + 1)
            .coerceAtMost(pageCount - 1)
    }

    fun previous() {
        currentPage = (currentPage - 1)
            .coerceAtLeast(0)
    }
}

@Composable
fun rememberDocumentPagerState(
    initialPage: Int = 0
): DocumentPagerState {

    return remember {
        DocumentPagerState(initialPage)
    }
}