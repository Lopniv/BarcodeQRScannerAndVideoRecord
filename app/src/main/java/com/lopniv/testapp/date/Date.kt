package com.lopniv.testapp.date

import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class Date()
{
    fun getCurrentDate(stringPattern: String): String
    {
        val date: Date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat(stringPattern, Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}
