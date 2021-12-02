package com.lopniv.testapp.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.lopniv.testapp.activities.base.BaseActivity
import com.lopniv.testapp.camera.CameraScanner
import com.lopniv.testapp.constants.IntConstants.INT_PERMISSION_CAMERA_REQUEST_CODE
import com.lopniv.testapp.constants.StringConstants.STRING_TAG_SCANNER
import com.lopniv.testapp.databinding.ActivityScannerBinding
import com.lopniv.testapp.viewmodels.CameraXViewModel
import java.util.concurrent.Executors

class ScannerActivity : BaseActivity<ActivityScannerBinding>()
{

    companion object
    {
        fun startScanner(context: Context) =
            context.startActivity(Intent(context, ScannerActivity::class.java))
    }


    //region DECLARATION

    private var _cameraScanner: CameraScanner? = null
    private var _cameraSelector: CameraSelector? = null
    private var _analysisUseCase: ImageAnalysis? = null

    //endregion


    override fun getViewBinding() = ActivityScannerBinding.inflate(layoutInflater)

    override fun startAction()
    {
        initViews()
        setupCamera()
    }

    private fun initViews()
    {
        _binding.floatingActionButtonClose.setOnClickListener { onBackPressed() }
    }

    private fun setupCamera()
    {
        _cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CameraXViewModel::class.java]
            .processCameraProvider
            .observe(this) { provider: ProcessCameraProvider? ->
                _cameraScanner = provider?.let()
                {
                    CameraScanner(
                        this,
                        _binding.previewView,
                        provider,
                        _cameraSelector,
                        this)
                }
                if (_cameraScanner?.checkCameraPermission(this) == true)
                {
                    bindCameraUseCases()
                }
                else
                {
                    _cameraScanner?.getCameraPermission(this)
                    Log.e(STRING_TAG_SCANNER, "No Camera Permission")
                }
            }
    }

    private fun bindCameraUseCases()
    {
        _cameraScanner?.bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun bindAnalyseUseCase()
    {
        _analysisUseCase = _cameraScanner?.bindAnalyseUseCase()
        val executorCamera = Executors.newSingleThreadExecutor()

        _analysisUseCase?.setAnalyzer(
            executorCamera,
            { imageProxy ->
                _cameraScanner?.getBarcodeScanner()?.let {
                    processImageProxy(
                        it, imageProxy)
                }
            }
        )
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    )
    {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener()
            { barcodes ->
                barcodes.forEach()
                {
                    ResultActivity.startResult(
                        this,
                        it.rawValue ?: "",
                        it.format
                    )
                    _cameraScanner?.stopCamera()
                    _cameraScanner?.stopScan()
                }
            }
            .addOnFailureListener()
            {
                Log.e(STRING_TAG_SCANNER, it.message ?: it.toString())
            }
            .addOnCompleteListener()
            {
                imageProxy.close()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )
    {
        if (requestCode == INT_PERMISSION_CAMERA_REQUEST_CODE)
        {
            if (_cameraScanner?.checkCameraPermission(this) == true)
            {
                bindCameraUseCases()
            }
            else
            {
                Log.e(STRING_TAG_SCANNER, "No Camera Permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume()
    {
        super.onResume()
        bindCameraUseCases()
    }

    override fun onPause()
    {
        super.onPause()
        _cameraScanner?.stopCamera()
        _cameraScanner?.stopScan()
    }
}