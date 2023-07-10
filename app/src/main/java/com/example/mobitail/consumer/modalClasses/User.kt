package com.example.mobitail.consumer.modalClasses

data class User(
    val first_name: String = "",
    val last_name: String = "",
    val e_mail: String = "",
    val pass: String = "",
    val deviceName: String = "",
    val deviceId: String = "",
    val contact: String = "",
    val location: String = ""
) {
    init {
        require(contact.length >= 10 || contact.isEmpty()) { "Contact should be at least 10 characters long." }
    }

    constructor() : this("", "", "", "", "")
}