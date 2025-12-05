package com.example.f1application.features.races.repository

import com.example.f1application.core.api.F1ApiService
import com.example.f1application.core.mappers.toDomain
import com.example.f1application.core.model.Race

class RacesRepository (
    private val apiService: F1ApiService
) {
    suspend fun getAllRaces(): List<Race> {
        val response = apiService.getAllRaces()
        return response.races.map { it.toDomain() }
    }
}