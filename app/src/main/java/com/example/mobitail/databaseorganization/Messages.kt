package com.example.mobitail.databaseorganization

data class Messages(
    val senderId: String = "",
    val recipientId: String = "",
    val isRetailer: Boolean = true
)