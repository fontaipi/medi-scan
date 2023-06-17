package com.fontaipi.mediscan.feature.scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val callback: (str: String?) -> Unit
) : ImageAnalysis.Analyzer {

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val scanner = BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .build()
            )
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(inputImage).addOnSuccessListener { barcodes ->
                if (barcodes.size > 0) {
                    val barcode = barcodes.first()
                    val cisCode = if (barcode.format == Barcode.FORMAT_DATA_MATRIX) {
                        barcode.rawValue?.substring(4)?.take(13)
                    } else {
                        barcode.rawValue
                    }
                    callback.invoke(
                        cisCode
                    )
                }
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }
}
