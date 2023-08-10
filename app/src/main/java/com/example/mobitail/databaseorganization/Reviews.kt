package com.example.mobitail.databaseorganization

data class Reviews(
    val review: String,
    val reviewerId: String,
    val date: String,
    val stars: Int,
    val storeid: Int
)