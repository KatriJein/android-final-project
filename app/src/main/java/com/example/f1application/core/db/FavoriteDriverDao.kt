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

    @Query("DELETE FROM favorite_drivers WHERE driverId = :driverId")
    suspend fun deleteById(driverId: String)

    @Query("SELECT * FROM favorite_drivers")
    suspend fun getAllFavorites(): List<FavoriteDriverEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_drivers WHERE driverId = :driverId)")
    suspend fun isFavorite(driverId: String): Boolean
}