package com.lopniv.testapp.functions

import android.Manifest
import android.app.Activity
import android.content.Context
import com.lopniv.testapp.constants.IntConstants
import com.lopniv.testapp.interfaces.CameraPermissionInterface

open class CameraPermissionFunction: CameraPermissionInterface
{

    override fun checkCameraPermissionIsGranted(context: Context): Boolean
    {
        return BasePermissionFunction().checkPermissionIsGranted(
            context, Manifest.permission.CAMERA
        )
    }

    override fun getCameraPermission(activity: Activity)
    {
        BasePermissionFunction().getPermission(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            IntConstants.INT_PERMISSION_CAMERA_REQUEST_CODE
        )
    }
}