package com.lopniv.testapp.functions

import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class DateFunction
{
    fun getCurrentDate(stringPattern: String): String
    {
        val date: Date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat(stringPattern, Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}
