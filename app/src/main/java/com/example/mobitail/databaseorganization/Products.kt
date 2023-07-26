package com.example.mobitail.databaseorganization

import java.io.Serializable


data class Products(
    val prodName: String? = "",
    val prodImage: String? = "",
    val prodBrand: String? = "",
    val prodDescription: String? = "",
    val prodPrice: Double? = 0.00,
    val quantity: Int? = 1,
    val sold: Int? = 0,
    var storeId: String? = "",
    val userid: String = "",
    val likes: Int? = 0,
    val reviews: Int? = 0,
    val dateAdded: String? = ""
): Serializable