package com.imrohansoni.docleaf.features.scanner.detection

import com.imrohansoni.docleaf.features.scanner.model.DetectedDocument
import com.imrohansoni.docleaf.features.scanner.model.Quad

class AutoCaptureController(
    private val requiredFrames: Int = 10,
    private val movementTolerance: Float = 0.035f,
    private val minConfidence: Float = 0.55f,
) {
    private var stableCount = 0
    private var lastQuad: Quad? = null

    fun feed(doc: DetectedDocument?): Float {
        if (doc == null || doc.confidence < minConfidence) { reset(); return 0f }
        val prev = lastQuad
        lastQuad = doc.corners
        if (prev != null) {
            val maxDim = maxOf(doc.imageWidth, doc.imageHeight).toFloat()
            if (maxMove(prev, doc.corners) > maxDim * movementTolerance) {
                stableCount = 0
                return 0f
            }
        }
        stableCount = (stableCount + 1).coerceAtMost(requiredFrames)
        return stableCount / requiredFrames.toFloat()
    }

    fun reset() { stableCount = 0; lastQuad = null }

    private fun maxMove(a: Quad, b: Quad) = maxOf(
        (a.topLeft - b.topLeft).getDistance(), (a.topRight - b.topRight).getDistance(),
        (a.bottomRight - b.bottomRight).getDistance(), (a.bottomLeft - b.bottomLeft).getDistance(),
    )
}