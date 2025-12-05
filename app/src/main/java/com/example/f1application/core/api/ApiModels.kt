package com.example.f1application.core.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiRacesResponse(
    val races: List<ApiRace>
)

@Serializable
data class ApiSingleRaceResponse(
    val race: List<ApiRace>
)

//@Serializable
//data class ApiDriversResponse(
//    val drivers: List<ApiDriver>
//)

// --- ApiRace ---
@Serializable
data class ApiRace(
    val raceId: String,
    val round: Int,
    val laps: Int,
    val raceName: String,
    val schedule: ApiSchedule,
    val circuit: ApiCircuit,
    val winner: ApiDriver?,
    val teamWinner: ApiTeam?,
    @SerialName("fast_lap") val fastLap: ApiFastLap?
)

@Serializable
data class ApiSchedule(val race: ApiSession, val fp1: ApiSession)

@Serializable
data class ApiSession(val date: String, val time: String?)

@Serializable
data class ApiCircuit(
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val city: String,
    @SerialName("circuitLength") val lengthStr: String,
    val corners: Int
)

// --- ApiDriver ---
@Serializable
data class ApiDriver(
    val driverId: String,
    val name: String,
    val surname: String,
    val shortName: String?,
    val number: Int?,
    val country: String,
    val birthday: String
//    val image: String?,
//    val points: String? // строка из API!
)

// --- ApiTeam ---
@Serializable
data class ApiTeam(
    val teamId: String,
    val teamName: String,
    val country: String,
    val firstAppearance: Int
)

// --- ApiFastLap ---
@Serializable
data class ApiFastLap(
    @SerialName("fast_lap") val time: String?,
    @SerialName("fast_lap_driver_id") val driverId: String?,
    @SerialName("fast_lap_team_id") val teamId: String?
)