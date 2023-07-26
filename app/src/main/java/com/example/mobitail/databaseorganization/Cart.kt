package com.example.mobitail.databaseorganization

import java.text.SimpleDateFormat
import java.util.Date

val currentDate = Date()
val dateFormat = SimpleDateFormat("dd-MM-yyyy")
val formattedDate = dateFormat.format(currentDate)

data class Cart(
    val userid: String? = "",
    val dateAdded: String? = formattedDate,
    val TotalCost: Double = 0.00
)