package com.example.mobitail.databaseorganization

data class Sales(
    val saleid: Int,
    val prodid: Int,
    val prodprice: Double,
    val quantity: Int,
    val totalcost: Double,
    val storeid: Int,
    val orderid: Int,
    val userid: Int,
    val date: String
)
