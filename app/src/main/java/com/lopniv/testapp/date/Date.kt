package com.lopniv.testapp.date

import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class Date()
{
    fun getCurrentDate(): String
    {
        val date: Date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}
