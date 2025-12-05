package com.example.f1application.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.ui.graphics.vector.ImageVector

interface TopLevelRoute : Route {
    val icon: ImageVector
}

data object Home : TopLevelRoute {
    override val icon = Icons.Default.Home
}

data object Races : TopLevelRoute {
    override val icon = Icons.Default.DateRange
}

data object Drivers : TopLevelRoute {
    override val icon = Icons.Default.SportsMotorsports
}

data object Profile : TopLevelRoute {
    override val icon = Icons.Default.Person
}
