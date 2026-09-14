package com.imrohansoni.docleaf.features.scanner.model

import com.imrohansoni.docleaf.core.components.IconResource
import com.imrohansoni.docleaf.core.components.Icons

enum class DocumentType(val title: String, val icon: IconResource) {
    PHOTO("Photo", Icons.UserSquare),
    ID_CARD("ID Card", Icons.IdCard),
    BOOK("Book", Icons.Book),
    FILES("Files", Icons.File),
    SIGNATURE("Signature", Icons.Signature),
    OCR("OCR", Icons.TextSquare),
    QR("QR", Icons.QrCode)
}