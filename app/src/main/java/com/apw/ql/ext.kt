package com.apw.ql

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toTorontoTime(): String? {
    val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return try {
        fromFormat.parse(this)?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }.format(it)
        }
    } catch (e: ParseException) {
        ""
    }
}







