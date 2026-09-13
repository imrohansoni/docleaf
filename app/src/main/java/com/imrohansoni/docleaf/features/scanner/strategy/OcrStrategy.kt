package com.imrohansoni.docleaf.features.scanner.strategy

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import com.imrohansoni.docleaf.features.scanner.model.ScanPayload
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

//class OcrStrategy(private val delegate: GenericDocumentStrategy) : DocumentStrategy by delegate {
//    override val type = DocumentType.OCR
//    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//
//    override suspend fun extract(cropped: Bitmap): ScanPayload = suspendCancellableCoroutine { cont ->
//        recognizer.process(InputImage.fromBitmap(cropped, 0))
//            .addOnSuccessListener { cont.resume(ScanPayload.Text(it.text)) }
//            .addOnFailureListener { cont.resume(ScanPayload.None) }
//    }
//
//    override fun close() { recognizer.close(); delegate.close() }
//}