package com.example.mobitail.databaseorganization

data class Products(
    val prodid: Int,
    val prodname: String,
    val prodimage: String,
    val prodlabel: String,
    val proddescription: String,
    val prodprice: Double,
    val quantity: Int,
    val storeid: Int,
    val likes: Int,
    val reviews: Int,
    val dateadded: String
)