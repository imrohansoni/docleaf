package com.imrohansoni.docleaf.features.scanner.strategy

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.imrohansoni.docleaf.features.scanner.detection.MlCornerDetector
import com.imrohansoni.docleaf.features.scanner.model.DetectedDocument
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import com.imrohansoni.docleaf.features.scanner.model.Edge
import com.imrohansoni.docleaf.features.scanner.model.Quad
import com.imrohansoni.docleaf.features.scanner.processing.DocumentImageProcessor
import com.imrohansoni.docleaf.features.scanner.utils.expandOutward
import kotlin.math.abs
//
//class GenericDocumentStrategy(
//    override val type: DocumentType,
//    private val liveDetector: MlCornerDetector,      // HEATMAP — fast
//    private val captureDetector: MlCornerDetector,   // SEGMENTATION — accurate
//    private val processor: DocumentImageProcessor,
//    /** Expected width/height, e.g. 1.586 for ID cards. Null = any shape. */
//    private val expectedAspect: Float? = null,
//    private val aspectTolerance: Float = 0.35f,
//    private val overlayExpand: Float = 0.015f,
//    private val cropExpand: Float = 0.03f,
//) : DocumentStrategy {
//
//    override suspend fun detectLive(bitmap: Bitmap): DetectedDocument? {
//        val ml = liveDetector.detect(bitmap, usePadding = false) ?: return null
//        if (!matchesAspect(ml.quad)) return null   // e.g. reject non-card shapes
//        val expanded = ml.quad.expandOutward(overlayExpand, bitmap.width, bitmap.height)
//        return DetectedDocument(
//            corners = expanded,
//            imageWidth = bitmap.width,
//            imageHeight = bitmap.height,
//            confidence = ml.confidence,
//            inferredEdges = frameBoundaryEdges(expanded, bitmap.width, bitmap.height),
//        )
//    }
//
//    override suspend fun detectCapture(bitmap: Bitmap): Quad? =
//        captureDetector.detect(bitmap)
//            ?.quad
//            ?.expandOutward(cropExpand, bitmap.width, bitmap.height)
//
//    override fun process(cropped: Bitmap): Bitmap = processor.process(cropped, type)
//
//    private fun matchesAspect(q: Quad): Boolean {
//        val expected = expectedAspect ?: return true
//        val w = (dist(q.topLeft, q.topRight) + dist(q.bottomLeft, q.bottomRight)) / 2f
//        val h = (dist(q.topLeft, q.bottomLeft) + dist(q.topRight, q.bottomRight)) / 2f
//        if (h < 1f) return false
//        val ratio = if (w > h) w / h else h / w
//        val norm = if (expected > 1f) expected else 1f / expected
//        return abs(ratio - norm) <= aspectTolerance
//    }
//
//    private fun dist(a: Offset, b: Offset) = (b - a).getDistance()
//
//    // moved verbatim out of the ViewModel
//    private fun frameBoundaryEdges(q: Quad, w: Int, h: Int): Set<Edge> {
//
//    }
//}