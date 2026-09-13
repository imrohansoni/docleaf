package com.imrohansoni.docleaf.features.scanner.strategy

import android.graphics.Bitmap
import com.imrohansoni.docleaf.features.scanner.model.*

/**
 * Everything that varies per document type lives behind this interface.
 * Adding a new type = adding one file here + one branch in the factory.
 * The ViewModel, overlay, camera and crop screens never change.
 */
interface DocumentStrategy {

    val type: DocumentType

    /** Show the drag-to-fix crop screen after capture? (QR: no.) */
    val requiresManualCrop: Boolean get() = true

    /** Allow the hold-steady auto-capture countdown? */
    val autoCaptureEnabled: Boolean get() = true

    /** Runs on every (2nd) live preview frame. Null = nothing detected. */
    suspend fun detectLive(bitmap: Bitmap): DetectedDocument?

    /** Runs once on the full-res captured frame. Null = fall back to live quad. */
    suspend fun detectCapture(bitmap: Bitmap): Quad?

    /** Enhance the cropped document (B&W, colour, sharpening…). */
    fun process(cropped: Bitmap): Bitmap

    /** Optional extraction after cropping — OCR text, QR value, etc. */
    suspend fun extract(cropped: Bitmap): ScanPayload = ScanPayload.None

    fun close() = Unit
}