package com.imrohansoni.docleaf.features.scanner.ui

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.imrohansoni.docleaf.core.components.FlipCameraButton
import com.imrohansoni.docleaf.features.scanner.ScannerViewModel
import com.imrohansoni.docleaf.features.scanner.camera.CameraBinder
import com.imrohansoni.docleaf.features.scanner.camera.CameraConfig
import com.imrohansoni.docleaf.features.scanner.camera.CameraFrameAnalyzer
import com.imrohansoni.docleaf.features.scanner.model.CaptureState
import com.imrohansoni.docleaf.features.scanner.model.ScanResult
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

/**
 * DocLeaf camera screen.
 *
 * Layout (root Box so FlyingThumbnail can travel outside the preview):
 *   Column
 *     ├─ CameraTopBar
 *     ├─ Preview 3:4  →  PreviewView + DocumentOverlay + GuideOverlay + step banner
 *     ├─ DocumentTypeSlider
 *     └─ BottomBar    →  CapturedStack | CaptureButton | SwitchCamera
 *   FlyingThumbnail (absolute, above everything)
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    vm: ScannerViewModel = viewModel(),
    onPagesReady: () -> Unit,                 // navigate to review/edit
    onPayload: (Any) -> Unit = {},            // QR value / OCR text sheet
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by vm.uiState.collectAsState()
    val liveDocument by vm.liveDocument.collectAsState()
    val isConfirmed by vm.isConfirmed.collectAsState()
    val captureState by vm.captureState.collectAsState()
    val shutter by vm.shutter.collectAsState()
//    val hint by vm.hint.collectAsState()
    val pages by vm.pages.collectAsState()
//    val instruction by vm.stepInstruction.collectAsState()
    val detectorError by vm.detectorError.collectAsState()

    var imageCaptureRef by remember { mutableStateOf<ImageCapture?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var flying by remember { mutableStateOf<Bitmap?>(null) }

    // One-shot events
    LaunchedEffect(Unit) { vm.flyingThumbnail.collect { flying = it } }
    LaunchedEffect(Unit) { vm.autoCaptureReady.collect { imageCaptureRef?.let(vm::captureDocument) } }
    LaunchedEffect(Unit) {
        vm.scanResult.collect { r ->
            when (r) {
                is ScanResult.Success -> onPagesReady()
//                is ScanResult.Payload -> onPayload(r.payload)
                is ScanResult.Failed -> Unit   // surface via your snackbar
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let(vm::importFromGallery) }

    // Permission gate
    val permission = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(Unit) { permission.launchPermissionRequest() }
    if (!permission.status.isGranted) {
        Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            BasicText("Camera permission required", style = TextStyle(color = Color.White))
        }
        return
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {

        when (val cs = captureState) {

            // ── Review / drag-to-fix crop ────────────────────────────────────
            is CaptureState.AwaitingCorrection -> ManualCropOverlay(
                bitmap = cs.rawBitmap,
                initialQuad = cs.detectedQuad,
                onConfirm = { quad -> vm.confirmCrop(cs.rawBitmap, quad) },
                onCancel = vm::cancelCrop,
            )

            is CaptureState.Processing -> Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
            ) { LoadingRing() }

            is CaptureState.Error -> Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
            ) {
                BasicText(cs.message, style = TextStyle(color = Color.White, fontSize = 15.sp))
                LaunchedEffect(Unit) { delay(1800); vm.dismissError() }
            }

            // ── Live camera ──────────────────────────────────────────────────
            else -> Column(Modifier.fillMaxSize()) {

                CameraTopBar(
                    flashEnabled = uiState.flashEnabled,
                    onToggleFlash = vm::toggleFlash,
                    onCameraSetting = {

                    },
                )

                // 3:4 preview — matches CameraConfig's RATIO_4_3
                Box(Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {

                    val previewView = remember {
                        PreviewView(context).apply {
                            // DocumentOverlay maps with maxOf(...) — must stay FILL_CENTER.
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }
                    }
                    val analyzerExecutor = remember { Executors.newSingleThreadExecutor() }

                    val imageAnalysis = remember {
                        ImageAnalysis.Builder()
                            .setResolutionSelector(CameraConfig.selector())
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                    }
                    val imageCapture = remember {
                        ImageCapture.Builder()
                            .setResolutionSelector(CameraConfig.selector())
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                            .build()
                    }

                    LaunchedEffect(uiState.isFrontCamera) {
                        imageAnalysis.setAnalyzer(analyzerExecutor, CameraFrameAnalyzer(vm::onCameraFrame))
                        CameraBinder.bind(
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                            previewView = previewView,
                            imageCapture = imageCapture,
                            imageAnalysis = imageAnalysis,
                            cameraSelector = if (uiState.isFrontCamera)
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            else CameraSelector.DEFAULT_BACK_CAMERA,
                        ) { cam -> camera = cam; imageCaptureRef = imageCapture }
                    }

                    LaunchedEffect(uiState.flashEnabled, camera) {
                        camera?.cameraControl?.enableTorch(uiState.flashEnabled)
                    }
                    DisposableEffect(Unit) { onDispose { analyzerExecutor.shutdown() } }

                    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

                    // Detected quad (green border, gradient, marching dashes)
                    DocumentOverlay(
                        document = liveDocument,
                        isConfirmed = isConfirmed,
                        isFrontCamera = uiState.isFrontCamera,
                        modifier = Modifier.fillMaxSize(),
                    )

                    // Per-strategy guide + coaching hint
//                    GuideOverlay(
//                        guide = vm.guide,
//                        hint = hint,
//                        modifier = Modifier.fillMaxSize(),
//                    )

                    // ID-card step banner ("Scan the FRONT of the card")
//                    AnimatedVisibility(
//                        visible = instruction.isNotEmpty(),
//                        enter = fadeIn(), exit = fadeOut(),
//                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
//                    ) {
//                        BasicText(
//                            instruction,
//                            style = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium),
//                            modifier = Modifier
//                                .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(20.dp))
//                                .padding(horizontal = 18.dp, vertical = 8.dp),
//                        )
//                    }

                    detectorError?.let {
                        BasicText(
                            it,
                            style = TextStyle(color = Color(0xFFFF5252), fontSize = 13.sp),
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = 56.dp),
                        )
                    }
                }

                DocumentTypeSlider(
                    currentDocumentType = uiState.documentType,
                    onClick = vm::setDocumentType,
                )

                Spacer(Modifier.weight(1f))

                // ── Bottom bar ───────────────────────────────────────────────
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                        .padding(bottom = 36.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CapturedStack(
                        pages = pages,
                        onClick = onPagesReady,
                        modifier = Modifier.align(Alignment.CenterStart),
                    )

                    CaptureButton(
                        state = shutter,
                        onCapture = { imageCaptureRef?.let(vm::onShutterTapped) },
                        modifier = Modifier.align(Alignment.Center),
                    )

                    FlipCameraButton {
                        vm.switchCamera()
                    }
                }
            }
        }

        // Above everything so it can fly from preview centre → bottom-left stack
        FlyingThumbnail(bitmap = flying, onFinished = { flying = null })
    }
}

/** Simple spinner without a Material dependency. */
@Composable
private fun LoadingRing() {
    val rotation by rememberInfiniteTransition(label = "spin").animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing)),
        label = "rot",
    )
    Canvas(Modifier.size(44.dp)) {
        drawArc(
            color = Color.White,
            startAngle = rotation, sweepAngle = 270f,
            useCenter = false,
            style = Stroke(3.dp.toPx(), cap = StrokeCap.Round),
        )
    }
}