package com.example.f1application.core.model

import androidx.compose.ui.graphics.Color

data class Team(
    val id: String,
    val name: String,
    val country: String,
    val imageUrl: String? = null,
    val points: Int? = null,
    val color: Color
)