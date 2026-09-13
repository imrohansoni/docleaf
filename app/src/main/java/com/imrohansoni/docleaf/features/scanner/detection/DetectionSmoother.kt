package com.imrohansoni.docleaf.features.scanner.detection

import androidx.compose.ui.geometry.Offset
import com.imrohansoni.docleaf.features.scanner.model.DetectedDocument
import com.imrohansoni.docleaf.features.scanner.model.Quad

class DetectionSmoother {
    companion object {
        private const val HISTORY_SIZE = 6
        private const val CONFIRM_FRAMES = 2
        private const val JUMP_THRESHOLD = 0.30f
        private const val DISAPPEAR_FRAMES = 14
        private const val STABLE_TOLERANCE = 0.18f
        private const val SMOOTH_ALPHA = 0.35f
    }

    private val history = ArrayDeque<DetectedDocument>(HISTORY_SIZE)
    private var confirmed: DetectedDocument? = null
    private var missCount = 0

    fun feed(new: DetectedDocument?, imgW: Int, imgH: Int): DetectedDocument? {
        if (new == null) {
            missCount++
            if (missCount >= DISAPPEAR_FRAMES) reset()
            return confirmed
        }
        missCount = 0

        confirmed?.let { prev ->
            val maxDim = maxOf(imgW, imgH).toFloat()
            if (maxCornerDistance(prev.corners, new.corners) > maxDim * JUMP_THRESHOLD) {
                history.clear(); confirmed = null
            }
        }

        if (history.size >= HISTORY_SIZE) history.removeFirst()
        history.addLast(new)
        if (history.size < CONFIRM_FRAMES) return confirmed

        val recent = history.takeLast(CONFIRM_FRAMES)
        if (!isStable(recent, imgW, imgH)) return confirmed

        // Smoothly follow the target instead of hard-replacing — this is what
        // makes the border track a moving camera instead of jumping.
        val target = average(recent)
        confirmed = if (confirmed == null) target
        else DetectedDocument(
            corners = lerpQuad(confirmed!!.corners, target.corners, SMOOTH_ALPHA),
            imageWidth = target.imageWidth,
            imageHeight = target.imageHeight,
            confidence = target.confidence,
            inferredEdges = target.inferredEdges,
        )
        return confirmed
    }

    fun lastConfirmed(): DetectedDocument? = confirmed
    fun reset() { history.clear(); confirmed = null; missCount = 0 }

    private fun lerpQuad(a: Quad, b: Quad, t: Float): Quad {
        fun l(p: Offset, q: Offset) = Offset(p.x + (q.x - p.x) * t, p.y + (q.y - p.y) * t)
        return Quad(
            l(a.topLeft, b.topLeft), l(a.topRight, b.topRight),
            l(a.bottomRight, b.bottomRight), l(a.bottomLeft, b.bottomLeft)
        )
    }

    private fun isStable(docs: List<DetectedDocument>, imgW: Int, imgH: Int): Boolean {
        val threshold = maxOf(imgW, imgH) * STABLE_TOLERANCE
        return listOf(
            docs.map { it.corners.topLeft }, docs.map { it.corners.topRight },
            docs.map { it.corners.bottomRight }, docs.map { it.corners.bottomLeft },
        ).all { corners ->
            val mx = corners.sumOf { it.x.toDouble() }.toFloat() / corners.size
            val my = corners.sumOf { it.y.toDouble() }.toFloat() / corners.size
            corners.maxOfOrNull { (it - Offset(mx, my)).getDistance() }!! <= threshold
        }
    }

    private fun average(docs: List<DetectedDocument>): DetectedDocument {
        val n = docs.size.toFloat()
        fun avg(pick: (Quad) -> Offset) = Offset(
            docs.sumOf { pick(it.corners).x.toDouble() }.toFloat() / n,
            docs.sumOf { pick(it.corners).y.toDouble() }.toFloat() / n,
        )
        return docs.last().copy(corners = Quad(
            avg { it.topLeft }, avg { it.topRight }, avg { it.bottomRight }, avg { it.bottomLeft },
        )
        )
    }

    private fun maxCornerDistance(a: Quad, b: Quad) = maxOf(
        (a.topLeft - b.topLeft).getDistance(), (a.topRight - b.topRight).getDistance(),
        (a.bottomRight - b.bottomRight).getDistance(), (a.bottomLeft - b.bottomLeft).getDistance(),
    )
}