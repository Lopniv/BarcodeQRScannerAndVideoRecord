package com.lopniv.testapp.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE

class Clipboard
{
    fun copyToClipboard(context: Context, stringData: String)
    {
        val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Result", stringData)
        clipboard.setPrimaryClip(clip)
    }
}