package com.lopniv.testapp.functions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lopniv.testapp.constants.IntConstants.INT_PERMISSION_CAMERA_REQUEST_CODE

open class CameraFunction
{

    /*
    Note :
    Class - Camera:
    An open class that can be inherit to CameraScanner class or others, implements OOP
    functions which can be flexible functions to implement in any class and any changes
    are made only in this class.
    */

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

    fun openSettingPermission(context: Context)
    {
        Toast.makeText(context, "You need to active camera permission to run this feature", Toast.LENGTH_SHORT).show()
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply()
            {
                data = Uri.fromParts("package", context.packageName, null)
            }
        )
    }
}