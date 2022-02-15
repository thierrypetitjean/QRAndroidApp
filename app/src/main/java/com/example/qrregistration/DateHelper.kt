package com.example.qrregistration

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {
     fun giveDate(): String? {
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(cal.getTime())
    }

    fun giveTime(): String? {
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:ss")
        return sdf.format(cal.getTime())
    }
}