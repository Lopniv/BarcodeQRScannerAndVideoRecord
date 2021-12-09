package com.lopniv.testapp.interfaces

import android.app.Activity
import android.content.Context

interface CameraPermissionInterface
{
    fun checkCameraPermissionIsGranted(context: Context): Boolean
    fun getCameraPermission(activity: Activity)
}