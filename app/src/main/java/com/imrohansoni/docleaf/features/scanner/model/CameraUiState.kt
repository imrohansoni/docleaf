package com.imrohansoni.docleaf.features.scanner.model

data class CameraUiState(
    val flashEnabled: Boolean = false,
    val captureMode: CaptureMode = CaptureMode.AUTO,
    val documentType: DocumentType = DocumentType.FILES,
    val isFrontCamera: Boolean = false,
    val isCapturing: Boolean = false,
)