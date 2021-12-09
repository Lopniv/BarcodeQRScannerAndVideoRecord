package com.lopniv.testapp.interfaces

import android.app.Activity
import android.content.Context

interface VideoRecordPermissionInterface
{
    fun checkAllPermissionIsGranted(context: Context): Boolean
    fun getAllPermission(activity: Activity)
}