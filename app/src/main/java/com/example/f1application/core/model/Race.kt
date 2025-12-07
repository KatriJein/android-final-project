package com.example.f1application.core.model

data class Race(
    val id: String,
    val round: Int,
    val laps: Int?,
    val name: String,
    val date: String,
    val dateString: String,
    val circuit: Circuit,
    val winner: Driver? = null,
    val teamWinner: Team? = null,
    val status: RaceStatus = RaceStatus.UPCOMING,
    val fastLap: FastLapInfo? = null
)