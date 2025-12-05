package com.example.f1application.features.home.repository

import com.example.f1application.core.api.F1ApiService
import com.example.f1application.core.mappers.toDomain
import com.example.f1application.core.model.Race

class HomeRepository(
    private val api: F1ApiService
) {
    suspend fun getNextRace(): Race = api.getNextRace().race.first().toDomain()
//    suspend fun getTopDrivers(count: Int = 3): List<Driver> {
//        val all = api.getDrivers().drivers.map { it.toDomain() }
//        return all.sortedByDescending { it.points ?: 0 }.take(count)
//    }
//    suspend fun getTopTeams(count: Int = 3): List<Team> {
//        // Пока API не отдаёт команды напрямую → можно взять из гонок или hardcode
//        return emptyList() // или реализовать позже
//    }
}