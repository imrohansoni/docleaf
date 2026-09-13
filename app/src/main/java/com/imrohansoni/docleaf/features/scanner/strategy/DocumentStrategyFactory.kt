package com.imrohansoni.docleaf.features.scanner.strategy

import com.imrohansoni.docleaf.features.scanner.detection.MlCornerDetector
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import com.imrohansoni.docleaf.features.scanner.processing.DocumentImageProcessor

//class DocumentStrategyFactory(
//    private val liveDetector: MlCornerDetector,
//    private val captureDetector: MlCornerDetector,
//    private val processor: DocumentImageProcessor,
//) {
//    fun create(type: DocumentType): DocumentStrategy = when (type) {
//        DocumentType.FILES,
//        DocumentType.PHOTO -> GenericDocumentStrategy(type, liveDetector, captureDetector, processor)
//
//        // ISO/IEC 7810 ID card ratio — rejects quads that aren't card-shaped
//        DocumentType.ID_CARD -> GenericDocumentStrategy(
//            type, liveDetector, captureDetector, processor, expectedAspect = 1.586f,
//        )
//
//        DocumentType.OCR -> OcrStrategy(
//            GenericDocumentStrategy(type, liveDetector, captureDetector, processor)
//        )
//
//        DocumentType.QR -> QrStrategy()
//
//        // WIP — see honest assessment below
//        DocumentType.BOOK -> BookStrategy(
//            GenericDocumentStrategy(type, liveDetector, captureDetector, processor)
//        )
//        DocumentType.SIGNATURE -> SignatureStrategy(processor)
//    }
//}