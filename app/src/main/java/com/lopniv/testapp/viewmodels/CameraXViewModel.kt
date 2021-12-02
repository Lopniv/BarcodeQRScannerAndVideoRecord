package com.lopniv.testapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lopniv.testapp.constants.StringConstants.STRING_TAG_CAMERA_X
import java.util.concurrent.ExecutionException

class CameraXViewModel(application: Application) : AndroidViewModel(application)
{
    private var _processCameraProviderMutableLiveData: MutableLiveData<ProcessCameraProvider>? = null

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get()
        {
            if (_processCameraProviderMutableLiveData == null)
            {
                _processCameraProviderMutableLiveData = MutableLiveData()
                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(getApplication())
                cameraProviderFuture.addListener(
                {
                    try
                    {
                        _processCameraProviderMutableLiveData?.setValue(cameraProviderFuture.get())
                    }
                    catch (e: ExecutionException)
                    {
                        Log.e(STRING_TAG_CAMERA_X, "Unhandled exception", e)
                    }
                    catch (e: InterruptedException)
                    {
                        Log.e(STRING_TAG_CAMERA_X, "Unhandled exception", e)
                    }
                },
                    ContextCompat.getMainExecutor(getApplication())
                )
            }
            return _processCameraProviderMutableLiveData ?: MutableLiveData()
        }
}