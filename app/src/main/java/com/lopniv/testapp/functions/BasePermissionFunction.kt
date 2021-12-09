package com.lopniv.testapp.functions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BasePermissionFunction
{

    fun checkPermissionIsGranted(context: Context, stringPermission: String): Boolean
    {
        return (ContextCompat.checkSelfPermission(context, stringPermission)
                == PackageManager.PERMISSION_GRANTED)
    }

    fun getPermission(activity: Activity, arrayPermission: Array<String>, intRequestCode: Int)
    {
        ActivityCompat.requestPermissions(
            activity,
            arrayPermission,
            intRequestCode
        )
    }
}