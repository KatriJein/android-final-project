package com.example.f1application.core.mappers

import androidx.compose.ui.graphics.Color
import com.example.f1application.core.api.ApiCircuit
import com.example.f1application.core.api.ApiConstructorStanding
import com.example.f1application.core.api.ApiDriver
import com.example.f1application.core.api.ApiDriverResult
import com.example.f1application.core.api.ApiDriverStanding
import com.example.f1application.core.api.ApiFastLap
import com.example.f1application.core.api.ApiRace
import com.example.f1application.core.api.ApiRaceResult
import com.example.f1application.core.api.ApiSprintResult
import com.example.f1application.core.api.ApiTeam
import com.example.f1application.core.model.Circuit
import com.example.f1application.core.model.ConstructorStanding
import com.example.f1application.core.model.Driver
import com.example.f1application.core.model.DriverResult
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.core.model.FastLapInfo
import com.example.f1application.core.model.Race
import com.example.f1application.core.model.RaceResult
import com.example.f1application.core.model.RaceStatus
import com.example.f1application.core.model.SprintResult
import com.example.f1application.core.model.Team
import com.example.f1application.shared.TeamAssets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun ApiRace.toDomain(): Race {
    val raceDate = try {
        LocalDate.parse(schedule.race.date, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        LocalDate.MAX
    }

    val currentDate = LocalDate.now()

    val status = when {
        raceDate.isBefore(currentDate) -> RaceStatus.FINISHED
        winner != null -> RaceStatus.FINISHED
        raceDate.isAfter(currentDate) -> RaceStatus.UPCOMING
        else -> RaceStatus.IN_PROGRESS
    }

    val formattedDate = formatRaceDate(
        fp1Date = schedule.fp1.date,
        raceDate = schedule.race.date
    )

    return Race(
        id = raceId,
        round = round,
        name = raceName ?: name ?: "",
        date = formatDateToDdMmYyyy(schedule.race.date) ?: date ?: "",
        dateString = formattedDate,
        circuit = circuit.toDomain(),
        winner = winner?.toDomain(),
        teamWinner = teamWinner?.toDomain(),
        status = status,
        fastLap = fastLap?.toDomain(),
        laps = laps
    )
}

private fun formatRaceDate(fp1Date: String?, raceDate: String?): String {
    return if (fp1Date != null && raceDate != null) {
        val fp1Formatted = formatDateToDdMmYyyy(fp1Date)
        val raceFormatted = formatDateToDdMmYyyy(raceDate)
        "$fp1Formatted - $raceFormatted"
    } else if (raceDate != null) {
        formatDateToDdMmYyyy(raceDate)
    } else {
        "Дата не указана"
    }
}

private fun formatDateToDdMmYyyy(dateString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val localDate = LocalDate.parse(dateString, inputFormatter)
        localDate.format(outputFormatter)
    } catch (e: Exception) {
        dateString
    }
}

fun ApiCircuit.toDomain(): Circuit {
    val lengthKm = if (lengthStr != null) {
        lengthStr
            .replace("km", "")
            .toFloatOrNull()
            ?.div(1000f)
    } else {
        length?.toFloat()
    } ?: 5.0f

    return Circuit(
        id = circuitId,
        name = circuitName ?: name ?: "",
        country = country,
        city = city,
        lengthKm = lengthKm,
        corners = corners ?: numberOfCorners ?: 0,
        imageUrl = TeamAssets.getCircuitImage(circuitId)
    )
}

fun ApiDriver.toDomain(): Driver {
    return Driver(
        id = driverId ?: "",
        firstName = name,
        lastName = surname,
        shortName = shortName,
        number = number,
        country = nationality ?: country ?: "Неизвестно",
        imageUrl = TeamAssets.getDriverPhoto(driverId ?: ""),
        points = null,
        position = null,
        wins = null,
        birthday = birthday
    )
}

fun ApiTeam.toDomain(): Team {
    return Team(
        id = teamId ?: "",
        name = teamName,
        country = country ?: teamNationality ?: "Неизвестно",
        points = null,
        position = null,
        wins = null,
        imageUrl = TeamAssets.getTeamLogo(teamId ?: ""),
        color = Color(TeamAssets.getTeamColor(teamId ?: ""))
    )
}

fun ApiFastLap.toDomain(): FastLapInfo {
    return FastLapInfo(
        time = time,
        driverId = driverId,
        teamId = teamId
    )
}

fun ApiDriverStanding.toDomain(): DriverStanding {
    return DriverStanding(
        position = position,
        points = points,
        wins = wins,
        driver = driver.toDomain(driverId = driverId, nationality = driver.nationality),
        team = team.toDomain()
    )
}

fun ApiDriver.toDomain(driverId: String, nationality: String?): Driver {
    return Driver(
        id = driverId,
        firstName = name,
        lastName = surname,
        shortName = shortName,
        number = number,
        country = nationality ?: "Неизвестно",
        imageUrl = TeamAssets.getDriverPhoto(driverId),
        points = null,
        position = null,
        wins = null,
        birthday = birthday
    )
}

fun ApiConstructorStanding.toDomain(): ConstructorStanding {
    return ConstructorStanding(
        position = position,
        points = points,
        wins = wins,
        team = team.toDomain(teamId = teamId)
    )
}

fun ApiTeam.toDomain(teamId: String): Team {
    return Team(
        id = teamId,
        name = teamName,
        country = country ?: teamNationality ?: "Неизвестно",
        points = null,
        position = null,
        wins = null,
        imageUrl = TeamAssets.getTeamLogo(teamId),
        color = Color(TeamAssets.getTeamColor(teamId))
    )
}

fun ApiDriverResult.toDomain(): DriverResult {
    return DriverResult(
        race = race.toDomain(),
        result = result.toDomain(),
        sprintResult = sprintResult?.toDomain()
    )
}

fun ApiRaceResult.toDomain(): RaceResult {
    return RaceResult(
        finishingPosition = finishingPosition,
        gridPosition = gridPosition,
        raceTime = raceTime,
        pointsObtained = pointsObtained,
        retired = retired
    )
}

fun ApiSprintResult.toDomain(): SprintResult {
    return SprintResult(
        finishingPosition = finishingPosition,
        gridPosition = gridPosition,
        raceTime = raceTime,
        pointsObtained = pointsObtained,
        retired = retired
    )
}