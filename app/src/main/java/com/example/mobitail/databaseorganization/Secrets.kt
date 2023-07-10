package com.example.mobitail.databaseorganization

data class Secrets(
    val key: List<Int>? = null,
    val initV: List<Int>? = null
) {
    constructor() : this(null, null)
}