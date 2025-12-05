package com.example.f1application.core.mappers

import androidx.compose.ui.graphics.Color
import com.example.f1application.core.api.*
import com.example.f1application.core.model.*
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
        name = raceName,
        date = formatDateToDdMmYyyy(schedule.race.date),
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
    val lengthKm = lengthStr
        .replace("km", "")
        .toFloatOrNull()?.div(1000f) ?: 5.0f

    return Circuit(
        id = circuitId,
        name = circuitName,
        country = country,
        city = city,
        lengthKm = lengthKm,
        corners = corners,
        imageUrl = TeamAssets.getCircuitImage(circuitId)
    )
}

fun ApiDriver.toDomain(): Driver {
    return Driver(
        id = driverId,
        firstName = name,
        lastName = surname,
        shortName = shortName,
        number = number,
        country = country,
        imageUrl = TeamAssets.getDriverPhoto(driverId),
        points = null,
        wins = null,
        birthday = birthday
    )
}

fun ApiTeam.toDomain(): Team {
    return Team(
        id = teamId,
        name = teamName,
        country = country,
        points = null,
        imageUrl = TeamAssets.getTeamLogo(teamId),
        color = Color(TeamAssets.getTeamColor(teamId))
    )
}

fun ApiFastLap.toDomain(): FastLapInfo {
    return FastLapInfo(
        time = time,
        driverId = driverId,
        teamId = teamId
    )
}