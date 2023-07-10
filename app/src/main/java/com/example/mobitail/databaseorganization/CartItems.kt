package com.example.mobitail.databaseorganization

data class CartItems(
    val itemid: Int,
    val prodid: Int,
    val prodprice: Double,
    val quantity: Int,
    val totalcost: Double,
    val userid: Int
)