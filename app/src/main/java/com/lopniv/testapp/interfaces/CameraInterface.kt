package com.lopniv.testapp.interfaces

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lopniv.testapp.constants.IntConstants.INT_PERMISSION_CAMERA_REQUEST_CODE

interface CameraInterface
{
    fun checkCameraPermission(context: Context): Boolean
    {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkCameraPresent(context: Context): Boolean
    {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    fun getCameraPermission(activity: Activity)
    {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            INT_PERMISSION_CAMERA_REQUEST_CODE
        )
    }
}