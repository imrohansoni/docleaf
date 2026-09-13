package com.imrohansoni.docleaf.features.scanner.model

/** Extra data some document types produce beyond the cropped image. */
sealed interface ScanPayload {
    data object None : ScanPayload
    data class Text(val value: String) : ScanPayload
    data class Qr(val value: String, val format: String) : ScanPayload
}