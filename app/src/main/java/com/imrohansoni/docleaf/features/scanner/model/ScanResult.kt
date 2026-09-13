package com.imrohansoni.docleaf.features.scanner.model

import android.net.Uri

sealed class ScanResult {
    data class Success(val uri: Uri) : ScanResult()
    data class Failed(val message: String) : ScanResult()
}