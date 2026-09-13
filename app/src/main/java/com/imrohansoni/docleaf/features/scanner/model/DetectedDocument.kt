package com.imrohansoni.docleaf.features.scanner.model

data class DetectedDocument(
    val corners: Quad,
    val imageWidth: Int,       // size of the bitmap this was detected in
    val imageHeight: Int,
    val confidence: Float,     // 0..1, straight from the model
    val inferredEdges: Set<Edge> = emptySet(),  // drawn dashed in the overlay
)