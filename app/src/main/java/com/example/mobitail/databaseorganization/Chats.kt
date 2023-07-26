package com.example.mobitail.databaseorganization

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Chats(
    val dateTime: String = getCurrentDateTimeWithMillis(),
    val meesageId: String = "",
    val senderId: String = "",
    val recipientId: String = "",
    val deviceId: String = "",
    val deviceName: String = Build.MODEL,
    val message: String = "",
    val isPath: Boolean = false
)

fun getCurrentDateTimeWithMillis(): String {
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    return dateFormat.format(currentDate)
}
