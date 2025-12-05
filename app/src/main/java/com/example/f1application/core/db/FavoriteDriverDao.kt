package com.example.f1application.core.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driver: FavoriteDriverEntity)

    @Delete
    suspend fun delete(driver: FavoriteDriverEntity)

    @Query("SELECT * FROM favorite_drivers")
    suspend fun getAllFavorites(): List<FavoriteDriverEntity>
}