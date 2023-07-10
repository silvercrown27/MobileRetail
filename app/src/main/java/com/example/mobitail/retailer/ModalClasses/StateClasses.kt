package com.example.mobitail.retailer.ModalClasses

data class Country(
    val name: String,
    val states: List<State>
)

data class State(
    val name: String,
    val counties: List<County>
)

data class County(
    val name: String
)