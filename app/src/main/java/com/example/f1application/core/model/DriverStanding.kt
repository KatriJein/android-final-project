package com.example.f1application.core.model

data class DriverStanding(
    val position: Int,
    val points: Int,
    val wins: Int,
    val driver: Driver,
    val team: Team
)