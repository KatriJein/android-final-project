package com.example.f1application.core.api

import retrofit2.http.GET
import retrofit2.http.Path

interface F1ApiService {
    @GET("current")
    suspend fun getAllRaces(): ApiRacesResponse

    @GET("current/drivers/{driverId}")
    suspend fun getDriverDetails(@Path("driverId") driverId: String): ApiDriverDetailsResponse

    @GET("current/drivers-championship")
    suspend fun getDriversChampionship(): ApiDriversChampionshipResponse

    @GET("current/constructors-championship")
    suspend fun getConstructorsChampionship(): ApiConstructorsChampionshipResponse
}