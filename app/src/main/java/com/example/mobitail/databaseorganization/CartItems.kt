package com.example.mobitail.databaseorganization

data class CartItems(
    val prodName: String? = "",
    val prodPrice: Double = 0.00,
    val prodImg: String? = "",
    val quantity: Int = 1,
    val totalCost: Double = 0.00,
    val prodId: String? = "",
    val cartId: String? = ""
)