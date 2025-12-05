package com.example.f1application.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_drivers")
data class FavoriteDriverEntity(
    @PrimaryKey val driverId: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String?,
    val teamId: String?,
    val teamName: String?
)