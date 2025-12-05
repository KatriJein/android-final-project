package com.example.f1application.core.api

import retrofit2.http.GET

interface F1ApiService {
    @GET("current/next")
    suspend fun getNextRace(): ApiSingleRaceResponse

    @GET("current")
    suspend fun getAllRaces(): ApiRacesResponse

//    @GET("drivers")
//    suspend fun getDrivers(): ApiDriversResponse
}