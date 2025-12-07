package com.example.f1application.core.model

data class DriverResult(
    val race: Race,
    val result: RaceResult,
    val sprintResult: SprintResult?
)

data class RaceResult(
    val finishingPosition: String,
    val gridPosition: Int,
    val raceTime: String,
    val pointsObtained: Int,
    val retired: String?
)

data class SprintResult(
    val finishingPosition: Int?,
    val gridPosition: Int,
    val raceTime: String,
    val pointsObtained: Int,
    val retired: String?
)