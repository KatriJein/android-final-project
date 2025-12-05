package com.example.f1application.core.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteDriverEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDriversDao(): FavoriteDriverDao
}