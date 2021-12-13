package com.lopniv.testapp.functions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.lopniv.testapp.constants.IntConstants
import com.lopniv.testapp.interfaces.VideoRecordPermissionInterface

open class VideoRecordPermissionFunction: VideoRecordPermissionInterface
{

    private fun checkBaseVideoRecordPermission(context: Context): Boolean
    {
        return if (!BasePermissionFunction().checkPermissionIsGranted(
                context, Manifest.permission.CAMERA
            ))
        {
            false
        }
        else (BasePermissionFunction().checkPermissionIsGranted(
            context, Manifest.permission.RECORD_AUDIO
        ))
    }

    override fun checkAllPermissionIsGranted(context: Context): Boolean
    {
        return when
        {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P ->
            {
                checkBaseVideoRecordPermission(context)
            }
            Build.VERSION.SDK_INT > Build.VERSION_CODES.O ->
            {
                return if (!BasePermissionFunction().checkPermissionIsGranted(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
                {
                    false
                }
                else checkBaseVideoRecordPermission(context)
            }
            else ->
            {
                return if (!BasePermissionFunction().checkPermissionIsGranted(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
                {
                    false
                }
                else if (!BasePermissionFunction().checkPermissionIsGranted(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ))
                {
                    false
                }
                else checkBaseVideoRecordPermission(context)
            }
        }
    }

    override fun getAllPermission(activity: Activity)
    {
        BasePermissionFunction().getPermission(
            activity,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ),
            IntConstants.INT_PERMISSION_VIDEO_RECORD_REQUEST_CODE
        )
    }
}