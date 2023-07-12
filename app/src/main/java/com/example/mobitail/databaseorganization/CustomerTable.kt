package com.example.mobitail.databaseorganization

data class CustomerTable(
    val username: String = "",
    val profileimage: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val contact: String = "",
    val location: String = "",
    val gender: String = "",
    val password: String = "",
    val doc: String = "",
    val deviceName: String = "",
    val deviceId: String = "",
    val docTimeZone: String = ""
) {
    init {
        require(contact.length >= 8 || contact.isEmpty()) { "Contact must be at least 8 characters long" }
    }
}
