package com.example.f1application.core.navigation

import com.example.f1application.core.model.Driver
import com.example.f1application.core.model.Race

interface Route

data class DriverDetails(val driver: Driver) : Route
data class RaceDetails(val race: Race) : Route
data object EditProfile : Route