package com.example.mobitail.databaseorganization

import java.io.Serializable


data class Products(
    val prodName: String? = "",
    val prodImage: String? = "",
    val prodBrand: String? = "",
    val prodDescription: String? = "",
    val prodPrice: Double? = null,
    val quantity: Int? = null,
    val sold: Int? = null,
    val storeId: String? = "",
    val userid: String = "",
    val likes: Int? = null,
    val reviews: Int? = null,
    val dateAdded: String? = ""
): Serializable