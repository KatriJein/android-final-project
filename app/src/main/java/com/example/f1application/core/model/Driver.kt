package com.example.f1application.core.model

data class Driver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val shortName: String? = null,
    val number: Int? = null,
    val country: String,
    val imageUrl: String? = null,
    val points: Int? = null,
    val wins: Int? = null
)