package com.fontaipi.mediscan.feature.scanner

import android.Manifest
import android.graphics.RectF
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.concurrent.Executors

@Composable
fun ScannerRoute(
    navigateToDrugDetails: (String) -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val cisCode by viewModel.cisCode.collectAsStateWithLifecycle()
    ScannerScreen(
        navigateToDetails = navigateToDrugDetails,
        searchCisCode = viewModel::searchCisCode,
        resetCisCode = viewModel::resetCisCode,
        cisCode = cisCode
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    navigateToDetails: (String) -> Unit,
    searchCisCode: (String) -> Unit,
    resetCisCode: () -> Unit,
    cisCode: String?
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val previewView = remember { PreviewView(context) }
    var lensDirection by rememberSaveable { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var torchEnabled by rememberSaveable { mutableStateOf(false) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var zoom by remember { mutableFloatStateOf(0f) }

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    LaunchedEffect(torchEnabled) {
        camera?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(torchEnabled)
            }
        }
    }
    LaunchedEffect(zoom) {
        camera?.cameraControl?.setLinearZoom(zoom)
    }
    LaunchedEffect(previewView, lensDirection) {
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
            .build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val executor = Executors.newSingleThreadExecutor()
            val cameraProvider: ProcessCameraProvider =
                cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setResolutionSelector(resolutionSelector)
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensDirection)
                .build()

            val imageAnalyser = ImageAnalysis.Builder()
                .setResolutionSelector(resolutionSelector)
                .build()
                .also {
                    it.setAnalyzer(
                        executor,
                        BarcodeAnalyzer { barcode ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (barcode != null) {
                                searchCisCode(barcode)
                            }
                            cameraProvider.unbindAll()
                        }
                    )
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    context as ComponentActivity,
                    cameraSelector,
                    imageAnalyser,
                    preview
                )
            } catch (exc: Exception) {
                Log.e("DEBUG", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    LaunchedEffect(cisCode) {
        if (cisCode != null) {
            navigateToDetails(cisCode)
            resetCisCode()
        }
    }

    Box {
        if (cameraPermissionState.status.isGranted) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        val overlayWidth = size.width
                        val overlayHeight = size.height
                        val boxWidth = overlayWidth * 0.65
                        val boxHeight = overlayHeight * 0.35
                        val cx = overlayWidth / 2
                        val cy = overlayHeight / 2
                        val rectF = RectF(
                            (cx - boxWidth / 2).toFloat(),
                            (cy - boxHeight / 2).toFloat(),
                            (cx + boxWidth / 2).toFloat(),
                            (cy + boxHeight / 2).toFloat()
                        )
                        val scrimPaint: Paint = Paint().apply {
                            color = Color(0xFF99000000)
                        }
                        val boxPaint: Paint = Paint().apply {
                            color = Color.White
                            style = PaintingStyle.Stroke
                            strokeWidth = 4f
                        }
                        val eraserPaint: Paint = Paint().apply {
                            strokeWidth = boxPaint.strokeWidth
                            blendMode = BlendMode.Clear
                        }

                        drawIntoCanvas { canvas ->
                            drawContent()
                            canvas.drawPath(
                                Path(),
                                Paint().apply {
                                    strokeWidth = 4f
                                    color = Color.White
                                }
                            )
                            canvas.drawRect(
                                left = 0f,
                                top = 0f,
                                right = overlayWidth,
                                bottom = overlayHeight,
                                paint = scrimPaint
                            )
                            eraserPaint.style = PaintingStyle.Fill
                            canvas.drawRoundRect(
                                left = rectF.left,
                                top = rectF.top,
                                right = rectF.right,
                                bottom = rectF.bottom,
                                8f,
                                8f,
                                eraserPaint
                            )
                            eraserPaint.style = PaintingStyle.Stroke
                            canvas.drawRoundRect(
                                left = rectF.left,
                                top = rectF.top,
                                right = rectF.right,
                                bottom = rectF.bottom,
                                8f,
                                8f,
                                eraserPaint
                            )
                            canvas.drawRoundRect(
                                left = rectF.left,
                                top = rectF.top,
                                right = rectF.right,
                                bottom = rectF.bottom,
                                8f,
                                8f,
                                eraserPaint
                            )
                            val path = createPath(
                                size.width,
                                size.height,
                                cx - boxWidth / 2,
                                cy - boxHeight / 2
                            )
                            canvas.drawPath(path, boxPaint)
                        }
                    }
            )
            Column {
                Row {
                    IconButton(onClick = {
                        lensDirection =
                            if (lensDirection == CameraSelector.LENS_FACING_BACK) {
                                CameraSelector.LENS_FACING_FRONT
                            } else {
                                CameraSelector.LENS_FACING_BACK
                            }
                    }) {
                        Icon(imageVector = Icons.Default.Sync, contentDescription = null)
                    }
                    IconButton(onClick = {
                        torchEnabled = !torchEnabled
                    }) {
                        Icon(
                            imageVector = if (torchEnabled) Icons.Default.FlashOff else Icons.Default.FlashOn,
                            contentDescription = null
                        )
                    }
                }
                Slider(value = zoom, onValueChange = { zoom = it }, valueRange = 0f..1f)
            }
        } else {
            Column {
                val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The camera is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}

fun createPath(width: Float, height: Float, mWidth: Double, mHeight: Double): Path {
    // draw border
    val lineHeight = 100f

    val path = Path()
    path.moveTo((width - mWidth).toFloat(), (mHeight).toFloat())
    path.lineTo(
        (width - mWidth).toFloat(),
        (mHeight + lineHeight).toFloat()
    )

    path.moveTo((width - mWidth).toFloat(), (mHeight).toFloat())
    path.lineTo(
        (width - mWidth).toFloat() - lineHeight,
        (mHeight).toFloat()
    )

    path.moveTo(
        (width - mWidth).toFloat(),
        (height - mHeight).toFloat()
    )
    path.lineTo(
        (width - mWidth).toFloat() - lineHeight,
        (height - mHeight).toFloat()
    )

    path.moveTo(
        (width - mWidth).toFloat(),
        (height - mHeight).toFloat()
    )
    path.lineTo(
        (width - mWidth).toFloat(),
        ((height - mHeight) - lineHeight).toFloat()
    )

    path.moveTo((mWidth).toFloat(), (mHeight).toFloat())
    path.lineTo(
        (mWidth).toFloat(),
        (mHeight + lineHeight).toFloat()
    )

    path.moveTo((mWidth).toFloat(), (mHeight).toFloat())
    path.lineTo(
        (mWidth).toFloat() + lineHeight,
        (mHeight).toFloat()
    )

    path.moveTo((mWidth).toFloat(), (height - mHeight).toFloat())
    path.lineTo(
        (mWidth).toFloat(),
        ((height - mHeight) - lineHeight).toFloat()
    )

    path.moveTo((mWidth).toFloat(), (height - mHeight).toFloat())
    path.lineTo(
        (mWidth).toFloat() + lineHeight,
        ((height - mHeight)).toFloat()
    )
    path.close()
    return path
}
