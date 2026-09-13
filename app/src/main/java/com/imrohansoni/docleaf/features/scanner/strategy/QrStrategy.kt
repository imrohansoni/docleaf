package com.imrohansoni.docleaf.features.scanner.strategy

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.imrohansoni.docleaf.features.scanner.model.DetectedDocument
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import com.imrohansoni.docleaf.features.scanner.model.Quad
import com.imrohansoni.docleaf.features.scanner.model.ScanPayload
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class QrStrategy : DocumentStrategy {
    override val type = DocumentType.QR
    override val requiresManualCrop = false      // nothing to crop
    override val autoCaptureEnabled = false      // fires on decode, not on stillness

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_ALL_FORMATS)
            .build()
    )

    /** Draw the overlay around the barcode's own corner points. */
    override suspend fun detectLive(bitmap: Bitmap): DetectedDocument? {
        val barcode = scanFirst(bitmap) ?: return null
        val pts = barcode.cornerPoints ?: return null
        if (pts.size < 4) return null
        return DetectedDocument(
            corners = Quad(
                Offset(pts[0].x.toFloat(), pts[0].y.toFloat()),
                Offset(pts[1].x.toFloat(), pts[1].y.toFloat()),
                Offset(pts[2].x.toFloat(), pts[2].y.toFloat()),
                Offset(pts[3].x.toFloat(), pts[3].y.toFloat()),
            ),
            imageWidth = bitmap.width,
            imageHeight = bitmap.height,
            confidence = 1f,
        )
    }

    override suspend fun detectCapture(bitmap: Bitmap): Quad? = null
    override fun process(cropped: Bitmap) = cropped

    override suspend fun extract(cropped: Bitmap): ScanPayload {
        val b = scanFirst(cropped) ?: return ScanPayload.None
        return ScanPayload.Qr(b.rawValue.orEmpty(), b.format.toString())
    }

    private suspend fun scanFirst(bmp: Bitmap): Barcode? = suspendCancellableCoroutine { cont ->
        scanner.process(InputImage.fromBitmap(bmp, 0))
            .addOnSuccessListener { cont.resume(it.firstOrNull()) }
            .addOnFailureListener { cont.resume(null) }
    }

    override fun close() { scanner.close() }
}