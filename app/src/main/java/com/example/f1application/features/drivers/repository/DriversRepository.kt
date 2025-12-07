package com.example.f1application.features.drivers.repository

import com.example.f1application.core.api.ApiDriverDetailsResponse
import com.example.f1application.core.api.F1ApiService
import com.example.f1application.core.mappers.toDomain
import com.example.f1application.core.model.DriverStanding

class DriversRepository(
    private val apiService: F1ApiService
) {
    suspend fun getDriversChampionship(): List<DriverStanding> {
        val response = apiService.getDriversChampionship()
        return response.driversChampionship.map { it.toDomain() }
    }

    suspend fun getDriverDetails(driverId: String): ApiDriverDetailsResponse {
        return apiService.getDriverDetails(driverId)
    }

    suspend fun getDriverStanding(driverId: String): DriverStanding {
        val allStandings = getDriversChampionship()
        return allStandings.first { it.driver.id == driverId }
    }
}