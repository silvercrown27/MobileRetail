package com.example.mobitail.databaseorganization

data class Products(
    val prodname: String? = "",
    val prodimage: String? = "",
    val prodlabel: String? = "",
    val proddescription: String? = "",
    val prodprice: Double? = null,
    val quantity: Int? = null,
    val storeid: Int? = null,
    val likes: Int? = null,
    val reviews: Int? = null,
    val dateadded: String? = ""
)