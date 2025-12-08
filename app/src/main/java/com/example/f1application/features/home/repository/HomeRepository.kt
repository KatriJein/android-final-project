package com.example.f1application.features.home.repository

import com.example.f1application.core.api.F1ApiService
import com.example.f1application.core.mappers.toDomain
import com.example.f1application.core.model.ConstructorStanding
import com.example.f1application.core.model.DriverStanding

class HomeRepository(
    private val apiService: F1ApiService
) {
    suspend fun getTopDrivers(count: Int): List<DriverStanding> {
        val response = apiService.getDriversChampionship()
        return response.driversChampionship
            .take(count)
            .map { it.toDomain() }
    }

    suspend fun getTopTeams(count: Int): List<ConstructorStanding> {
        val response = apiService.getConstructorsChampionship()
        return response.constructorsChampionship
            .take(count)
            .map { it.toDomain() }
    }
}