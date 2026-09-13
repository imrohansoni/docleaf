package com.imrohansoni.docleaf.features.scanner.model

import androidx.compose.ui.geometry.Offset


/** The 4 corners of a detected document, always ordered TL → TR → BR → BL. */
data class Quad(
    val topLeft: Offset,
    val topRight: Offset,
    val bottomRight: Offset,
    val bottomLeft: Offset,
) {
    fun toList() = listOf(topLeft, topRight, bottomRight, bottomLeft)
}