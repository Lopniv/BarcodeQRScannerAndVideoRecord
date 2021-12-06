package com.lopniv.testapp.functions

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.lopniv.testapp.constants.DoubleConstants
import com.lopniv.testapp.constants.StringConstants.STRING_TAG_CAMERA_SCANNER
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraFunctionScanner(
    private val _activity: Activity,
    private val _previewView: PreviewView,
    private val _processCameraProvider: ProcessCameraProvider,
    private val _lifecycleOwner: LifecycleOwner
): CameraFunction()
{


    //region DECLARATION

    private var _previewUseCase: Preview? = null
    private var _imageAnalysisUseCase: ImageAnalysis? = null
    private var _cameraSelector: CameraSelector? = null

    //endregion


    init
    {
        _cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    }

    private val _intScreenAspectRatio: Int
        get()
        {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics: WindowMetrics = _activity.windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                aspectRatio(
                    windowMetrics.bounds.width() - insets.left - insets.right,
                    windowMetrics.bounds.height() - insets.top - insets.bottom
                )
            } else {
                val displayMetrics = DisplayMetrics().also { _previewView.display?.getRealMetrics(it) }
                aspectRatio(displayMetrics.widthPixels, displayMetrics.heightPixels)
            }
        }

    private fun aspectRatio(width: Int, height: Int): Int
    {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        return if (abs(previewRatio - DoubleConstants.DOUBLE_RATIO_4_3_VALUE) <= abs(previewRatio - DoubleConstants.DOUBLE_RATIO_16_9_VALUE))
        {
            AspectRatio.RATIO_4_3
        }
        else AspectRatio.RATIO_16_9
    }

    fun stopCamera()
    {
        if (_previewUseCase != null) {
            _processCameraProvider.unbind(_previewUseCase)
        }
    }

    fun stopScan()
    {
        if (_imageAnalysisUseCase != null) {
            _processCameraProvider.unbind(_imageAnalysisUseCase)
        }
    }

    fun bindPreviewUseCase()
    {
        stopCamera()
        _previewUseCase = Preview.Builder()
            .setTargetAspectRatio(_intScreenAspectRatio)
            .setTargetRotation(_previewView.display?.rotation ?: 0)
            .build()
        _previewUseCase?.setSurfaceProvider(_previewView.surfaceProvider)

        try
        {
            _cameraSelector.let {
                if (it != null) {
                    _processCameraProvider.bindToLifecycle(
                        _lifecycleOwner,
                        it,
                        _previewUseCase
                    )
                }
            }
        }
        catch (illegalStateException: IllegalStateException)
        {
            Log.e(STRING_TAG_CAMERA_SCANNER, illegalStateException.message ?: "IllegalStateException")
        }
        catch (illegalArgumentException: IllegalArgumentException)
        {
            Log.e(STRING_TAG_CAMERA_SCANNER, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    fun getBarcodeScanner(): BarcodeScanner
    {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS
            )
            .build()

        return BarcodeScanning.getClient(options)
    }

    fun bindAnalyseUseCase(): ImageAnalysis
    {
        stopScan()
        _imageAnalysisUseCase = ImageAnalysis.Builder()
            .setTargetResolution(Size(100, 100))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(_previewView.display?.rotation ?: 0)
            .build()

        try
        {
            _cameraSelector.let {
                if (it != null) {
                    _processCameraProvider.bindToLifecycle(
                        _lifecycleOwner,
                        it,
                        _imageAnalysisUseCase
                    )
                }
            }
        }
        catch (illegalStateException: IllegalStateException)
        {
            Log.e(STRING_TAG_CAMERA_SCANNER, illegalStateException.message ?: "IllegalStateException")
        }
        catch (illegalArgumentException: IllegalArgumentException)
        {
            Log.e(STRING_TAG_CAMERA_SCANNER, illegalArgumentException.message ?: "IllegalArgumentException")
        }

        return _imageAnalysisUseCase as ImageAnalysis
    }
}