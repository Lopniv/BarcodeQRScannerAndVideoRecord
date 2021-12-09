package com.lopniv.testapp.functions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

class SettingFunction {

    fun openSettingPermission(context: Context)
    {
        Toast.makeText(context, "You need this permission to run the feature", Toast.LENGTH_SHORT).show()
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply()
            {
                data = Uri.fromParts("package", context.packageName, null)
            }
        )
    }
}
