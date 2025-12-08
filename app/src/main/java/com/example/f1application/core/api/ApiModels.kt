package com.example.f1application.core.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.f1application.shared.FinishingPositionSerializer

@Serializable
data class ApiRacesResponse(
    val races: List<ApiRace>
)

@Serializable
data class ApiRace(
    val raceId: String,
    val round: Int,
    val laps: Int?,
    val date: String?,
    val raceName: String?,
    val name: String?,
    val schedule: ApiSchedule?,
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
    val circuitName: String?,
    val name: String?,
    val country: String,
    val city: String,
    @SerialName("circuitLength") val lengthStr: String?,
    val length: Int?,
    val corners: Int?,
    val numberOfCorners: Int?
)

@Serializable
data class ApiDriver(
    val driverId: String?,
    val name: String,
    val surname: String,
    val shortName: String?,
    val number: Int?,
    val nationality: String?,
    val country: String?,
    val birthday: String
)

@Serializable
data class ApiTeam(
    val teamId: String?,
    val teamName: String,
    val country: String?,
    val teamNationality: String?
)

@Serializable
data class ApiFastLap(
    @SerialName("fast_lap") val time: String?,
    @SerialName("fast_lap_driver_id") val driverId: String?,
    @SerialName("fast_lap_team_id") val teamId: String?
)

// Driver Details
@Serializable
data class ApiDriverDetailsResponse(
    val driver: ApiDriver,
    val team: ApiTeam,
    val results: List<ApiDriverResult>
)

@Serializable
data class ApiDriverResult(
    val race: ApiRace,
    val result: ApiRaceResult,
    val sprintResult: ApiSprintResult?
)

@Serializable
data class ApiRaceResult(
    @Serializable(with = FinishingPositionSerializer::class)
    val finishingPosition: String,
    @Serializable(with = FinishingPositionSerializer::class)
    val gridPosition: String,
    val raceTime: String,
    val pointsObtained: Int,
    val retired: String?
)

@Serializable
data class ApiSprintResult(
    @Serializable(with = FinishingPositionSerializer::class)
    val finishingPosition: String?,
    val gridPosition: Int,
    val raceTime: String?,
    val pointsObtained: Int,
    val retired: String?
)

// Drivers Championship
@Serializable
data class ApiDriversChampionshipResponse(
    @SerialName("drivers_championship") val driversChampionship: List<ApiDriverStanding>
)

@Serializable
data class ApiDriverStanding(
    val position: Int,
    val points: Int,
    val wins: Int,
    val driverId: String,
    val driver: ApiDriver,
    val team: ApiTeam
)

// Constructors Championship
@Serializable
data class ApiConstructorsChampionshipResponse(
    @SerialName("constructors_championship") val constructorsChampionship: List<ApiConstructorStanding>
)

@Serializable
data class ApiConstructorStanding(
    val position: Int,
    val points: Int,
    val wins: Int,
    val teamId: String,
    val team: ApiTeam
)