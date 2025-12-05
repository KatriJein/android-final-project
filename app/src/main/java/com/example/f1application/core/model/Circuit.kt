package com.example.f1application.core.model

data class Circuit(
    val id: String,
    val name: String,
    val country: String,
    val city: String,
    val lengthKm: Float,
    val corners: Int,
    val imageUrl: String? = null
)