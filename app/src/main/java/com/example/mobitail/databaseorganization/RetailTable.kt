package com.example.mobitail.databaseorganization

data class RetailTable(
    val profileimage: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val contact: String,
    val password: String,
    val doc: String
) {
    init {
        require(password.length >= 8) { "Password must be at least 8 characters long" }
        require(contact.length >= 10 || contact.isEmpty()) { "Contact must be at least 8 characters long" }
        require(email.matches(Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")))
        { "Invalid email format" }
    }
    constructor() : this("", "", "", "", "", "", "")
}