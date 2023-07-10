package com.example.mobitail.consumer.modalClasses

data class ProfileItems (
    val imageResId: Int,
    val label: String,
    val description: String,
    val activity: Class<*>
)