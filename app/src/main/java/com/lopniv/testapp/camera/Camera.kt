package com.lopniv.testapp.camera

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

abstract class Camera
{

    /*
    Class - Camera
    Description : An abstract class that can be inherit to other camera classes, implements OOP
                  functions which can be flexible functions to implement in any class and any changes
                  are made only in this class.
    Author      : Abdullah Fahmi.
    Created on  : Friday, 03 December 2021.           Updated on : Friday, 03 December 2021.
    Created by  : Abdullah Fahmi.                     Updated by : Abdullah Fahmi.
    Version     : 1.0.:0
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