package com.example.mobitail.databaseorganization

data class Orders(
    val orderid: Int,
    val date: String,
    val totalitems: Int,
    val totalprice: Double,
    val storeid: Int,
    val userid: Int
)