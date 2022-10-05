package com.tenqube.sample.syrup.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getDateTime(time: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentTime = Date(time)
        return formatter.format(currentTime)
    }
}