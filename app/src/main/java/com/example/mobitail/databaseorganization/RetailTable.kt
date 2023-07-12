package com.example.mobitail.databaseorganization

data class RetailTable(
    val profileimage: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val contact: String = "",
    val gender: String = "",
    val location: String = "",
    val doc: String = "",
    val deviceName: String = "",
    val deviceId: String = "",
    val docTimeZone: String = ""
) {
    init {
        require(contact.length >= 10 || contact.isEmpty()) { "Contact must be at least 8 characters long" }
    }
}