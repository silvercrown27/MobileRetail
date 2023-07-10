package com.example.mobitail.databaseorganization

data class Reviews(
    val revid: Int,
    val review: String,
    val date: String,
    val stars: Int,
    val storeid: Int
)