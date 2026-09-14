package com.imrohansoni.docleaf.features.scanner

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Offset
import androidx.core.graphics.scale
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.imrohansoni.docleaf.features.scanner.detection.AutoCaptureController
import com.imrohansoni.docleaf.features.scanner.detection.DetectionSmoother
import com.imrohansoni.docleaf.features.scanner.detection.MlCornerDetector
import com.imrohansoni.docleaf.features.scanner.model.CameraUiState
import com.imrohansoni.docleaf.features.scanner.model.CaptureMode
import com.imrohansoni.docleaf.features.scanner.model.CaptureState
import com.imrohansoni.docleaf.features.scanner.model.DetectedDocument
import com.imrohansoni.docleaf.features.scanner.model.DocumentType
import com.imrohansoni.docleaf.features.scanner.model.Edge
import com.imrohansoni.docleaf.features.scanner.model.Quad
import com.imrohansoni.docleaf.features.scanner.model.ScanResult
import com.imrohansoni.docleaf.features.scanner.model.ShutterState
import com.imrohansoni.docleaf.features.scanner.processing.DocumentImageProcessor
import com.imrohansoni.docleaf.features.scanner.processing.PerspectiveCorrector
import com.imrohansoni.docleaf.features.scanner.utils.GalleryImageLoader
import com.imrohansoni.docleaf.features.scanner.utils.expandOutward
import com.imrohansoni.docleaf.features.scanner.utils.rotate
import com.imrohansoni.docleaf.features.scanner.utils.scaleTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil


class ScannerViewModel(app: Application) : AndroidViewModel(app) {
    companion object {
        private const val TAG = "ScanPipeline"
        private const val OVERLAY_EXPAND = 0.015f
        private const val CROP_EXPAND = 0.03f
    }

    private val liveDetector by lazy {
        MlCornerDetector(app, "fastvit_t8_h_e_bifpn_256_fp32.onnx", MlCornerDetector.Mode.HEATMAP)
    }
    private val captureDetector by lazy {
        MlCornerDetector(app, "u2net_int8.onnx", MlCornerDetector.Mode.SEGMENTATION)
    }

    private val smoother = DetectionSmoother()
    private val autoCapture = AutoCaptureController()
    private val corrector = PerspectiveCorrector()
    private val processor = DocumentImageProcessor()
    private val captureExecutor = Executors.newSingleThreadExecutor()

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val _liveDocument = MutableStateFlow<DetectedDocument?>(null)
    val liveDocument: StateFlow<DetectedDocument?> = _liveDocument.asStateFlow()

    private val _isConfirmed = MutableStateFlow(false)
    val isConfirmed: StateFlow<Boolean> = _isConfirmed.asStateFlow()

    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState: StateFlow<CaptureState> = _captureState.asStateFlow()

    private val _autoCaptureProgress = MutableStateFlow(0f)
    val autoCaptureProgress: StateFlow<Float> = _autoCaptureProgress.asStateFlow()

    private val _autoCaptureReady = MutableSharedFlow<Unit>()
    val autoCaptureReady: SharedFlow<Unit> = _autoCaptureReady.asSharedFlow()

    private val _scanResult = MutableSharedFlow<ScanResult>()
    val scanResult: SharedFlow<ScanResult> = _scanResult.asSharedFlow()

    private val _detectorError = MutableStateFlow<String?>(null)
    val detectorError: StateFlow<String?> = _detectorError.asStateFlow()

    private val analyzing = AtomicBoolean(false)
    private var autoCaptureFired = false
    private var frameCount = 0
    private var skipFrame = false   // process every 2nd frame


    private val _shutter = MutableStateFlow<ShutterState>(ShutterState.Idle)
    val shutter: StateFlow<ShutterState> = _shutter.asStateFlow()

    private val _pages = MutableStateFlow<List<Bitmap>>(emptyList())
    val pages: StateFlow<List<Bitmap>> = _pages.asStateFlow()

    private val _flyingThumbnail = MutableSharedFlow<Bitmap>()
    val flyingThumbnail: SharedFlow<Bitmap> = _flyingThumbnail.asSharedFlow()

    private var countdownJob: Job? = null
    private val countdownMillis = 3_000L

    private fun startCountdown() {
        if (countdownJob?.isActive == true) return
        countdownJob = viewModelScope.launch {
            val step = 50L
            var elapsed = 0L
            while (elapsed < countdownMillis) {
                if (!_isConfirmed.value) {
                    _shutter.value = ShutterState.Idle
                    return@launch
                }
                val progress = elapsed / countdownMillis.toFloat()
                val secondsLeft = ceil((countdownMillis - elapsed) / 1000.0).toInt()
                _shutter.value = ShutterState.Countdown(secondsLeft, progress)
                delay(step)
                elapsed += step
            }
            _shutter.value = ShutterState.Capturing
            _autoCaptureReady.emit(Unit)
        }
    }

    private fun cancelCountdown() {
        countdownJob?.cancel()
        countdownJob = null
        if (_shutter.value !is ShutterState.Capturing) _shutter.value = ShutterState.Idle
    }

    fun onShutterTapped(imageCapture: ImageCapture) {
        cancelCountdown()
        _shutter.value = ShutterState.Capturing
        captureDocument(imageCapture)
    }



    // ── Live frames ────────────────────────────────────────────────────────────
    fun onCameraFrame(bitmap: Bitmap) {
        if (!analyzing.compareAndSet(false, true)) return

        skipFrame = !skipFrame
        if (skipFrame) { analyzing.set(false); return }

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val ml = runCatching { liveDetector.detect(bitmap) }
                    .onFailure {
                        _detectorError.value = "Model failed to load — check assets/ filenames"
                        Log.e(TAG, "detector error", it)
                    }
                    .getOrNull()

                val raw = ml?.let {
                    val expanded = it.quad.expandOutward(OVERLAY_EXPAND, bitmap.width, bitmap.height)
                    DetectedDocument(
                        corners = expanded,
                        imageWidth = bitmap.width,
                        imageHeight = bitmap.height,
                        confidence = it.confidence,
                        inferredEdges = frameBoundaryEdges(expanded, bitmap.width, bitmap.height),
                    )
                }

                val confirmed = smoother.feed(raw, bitmap.width, bitmap.height)

                _liveDocument.value = confirmed ?: raw
                _isConfirmed.value = confirmed != null

                if (++frameCount % 15 == 0) {
                    Log.d(TAG, "model=${ml != null} conf=${"%.2f".format(ml?.confidence ?: 0f)} " +
                            "confirmed=${confirmed != null} frame=${bitmap.width}x${bitmap.height} " +
                            "autoProgress=${"%.2f".format(_autoCaptureProgress.value)}")
                }

                if (_uiState.value.captureMode == CaptureMode.AUTO &&
                    _captureState.value is CaptureState.Idle &&
                    !_uiState.value.isCapturing
                ) {
                    val progress = autoCapture.feed(confirmed)
                    _autoCaptureProgress.value = progress
                    if (progress >= 1f && !autoCaptureFired) {
                        autoCaptureFired = true
                        _autoCaptureReady.emit(Unit)
                    }
                } else {
                    autoCapture.reset()
                    _autoCaptureProgress.value = 0f
                }
            } finally {
                analyzing.set(false)
            }
        }
    }

    private fun frameBoundaryEdges(q: Quad, w: Int, h: Int): Set<Edge> {
        val m = 0.02f
        val edges = mutableSetOf<Edge>()
        if (q.topLeft.y <= h * m && q.topRight.y <= h * m) edges += Edge.TOP
        if (q.bottomLeft.y >= h * (1 - m) && q.bottomRight.y >= h * (1 - m)) edges += Edge.BOTTOM
        if (q.topLeft.x <= w * m && q.bottomLeft.x <= w * m) edges += Edge.LEFT
        if (q.topRight.x >= w * (1 - m) && q.bottomRight.x >= w * (1 - m)) edges += Edge.RIGHT
        return edges
    }

    fun toggleFlash() = _uiState.update { it.copy(flashEnabled = !it.flashEnabled) }
    fun setCaptureMode(m: CaptureMode) { _uiState.update { it.copy(captureMode = m) }; autoCapture.reset() }
    fun setDocumentType(t: DocumentType) {
        _uiState.update { it.copy(documentType = t) }
        smoother.reset(); autoCapture.reset()
        _liveDocument.value = null; _isConfirmed.value = false
    }
    fun switchCamera() {
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
        smoother.reset(); autoCapture.reset()
        _liveDocument.value = null; _isConfirmed.value = false
    }

    fun captureDocument(imageCapture: ImageCapture) {
        if (_uiState.value.isCapturing) return
        _uiState.update { it.copy(isCapturing = true) }

        imageCapture.takePicture(captureExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(proxy: ImageProxy) {
                val bmp = proxy.toBitmap().rotate(proxy.imageInfo.rotationDegrees.toFloat())
                proxy.close()
                detectAndReview(bmp)
            }
            override fun onError(e: ImageCaptureException) {
                _uiState.update { it.copy(isCapturing = false) }
                autoCaptureFired = false
                viewModelScope.launch { _scanResult.emit(ScanResult.Failed(e.message ?: "Capture failed")) }
            }
        })
    }

    fun importFromGallery(uri: Uri) {
        viewModelScope.launch(Dispatchers.Default) {
            _captureState.value = CaptureState.Processing
            val bmp = GalleryImageLoader.load(getApplication(), uri)
            if (bmp == null) {
                _captureState.value = CaptureState.Error("Couldn't open that image")
                return@launch
            }
            detectAndReview(bmp)
        }
    }

    private fun detectAndReview(raw: Bitmap) {
        viewModelScope.launch(Dispatchers.Default) {
            val detected = runCatching { captureDetector.detect(raw) }.getOrNull()
            val last = smoother.lastConfirmed()

            val quad = when {
                detected != null -> {
                    Log.d(TAG, "review quad from CAPTURE model")
                    detected.quad.expandOutward(CROP_EXPAND, raw.width, raw.height)
                }
                last != null -> {
                    Log.d(TAG, "review quad from LIVE (rescaled ${last.imageWidth}x${last.imageHeight}→${raw.width}x${raw.height})")
                    last.corners
                        .scaleTo(last.imageWidth, last.imageHeight, raw.width, raw.height)
                        .expandOutward(CROP_EXPAND, raw.width, raw.height)
                }
                else -> {
                    Log.d(TAG, "review quad DEFAULT")
                    defaultQuad(raw.width, raw.height)
                }
            }

            _uiState.update { it.copy(isCapturing = false) }
            autoCaptureFired = false
            _captureState.value = CaptureState.AwaitingCorrection(raw, quad)
        }
    }

    fun confirmCrop(rawBitmap: Bitmap, quad: Quad) {
        viewModelScope.launch(Dispatchers.Default) {
            _captureState.value = CaptureState.Processing
            runCatching {
                val cropped = corrector.correct(rawBitmap, quad)
                val enhanced = processor.process(cropped, _uiState.value.documentType)
                val uri = saveToGallery(enhanced)
                enhanced to uri
            }.fold(
                onSuccess = { (enhanced, uri) ->
                    val thumb = enhanced.thumbnail()
                    _pages.update { it + thumb }
                    _flyingThumbnail.emit(thumb)
                    _shutter.value = ShutterState.Idle
                    _captureState.value = CaptureState.Idle
                    _scanResult.emit(ScanResult.Success(uri))
                },
                onFailure = { e ->
                    _shutter.value = ShutterState.Idle
                    _captureState.value = CaptureState.Idle
                    _scanResult.emit(ScanResult.Failed(e.message ?: "Processing failed"))
                },
            )
        }
    }

    fun cancelCrop() {
        _captureState.value = CaptureState.Idle
        _shutter.value = ShutterState.Idle      // ← add
        smoother.reset()
        autoCapture.reset()
        cancelCountdown()
        _liveDocument.value = null
        _isConfirmed.value = false
        _autoCaptureProgress.value = 0f
    }

    fun dismissError() { _captureState.value = CaptureState.Idle }

    private fun defaultQuad(w: Int, h: Int): Quad {
        val mx = w * 0.08f; val my = h * 0.08f
        return Quad(Offset(mx, my), Offset(w - mx, my), Offset(w - mx, h - my), Offset(mx, h - my))
    }

    private fun saveToGallery(bitmap: Bitmap): Uri {
        val resolver = getApplication<Application>().contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "scan_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DocumentScanner")
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: error("Could not create gallery entry")
        resolver.openOutputStream(uri)?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it) }
            ?: error("Could not write file")
        return uri
    }

    override fun onCleared() {
        super.onCleared()
        runCatching { liveDetector.close() }
        runCatching { captureDetector.close() }
        captureExecutor.shutdown()
    }
}

private fun Bitmap.thumbnail(width: Int = 200): Bitmap =
    scale(width, (width.toFloat() / this.width * height).toInt())