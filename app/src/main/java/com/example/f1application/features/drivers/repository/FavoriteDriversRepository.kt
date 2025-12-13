package com.example.f1application.features.drivers.repository

import com.example.f1application.core.db.AppDatabase
import com.example.f1application.core.db.FavoriteDriverEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteDriversRepository(
    private val db: AppDatabase
) {
    suspend fun isFavorite(driverId: String): Boolean =
        withContext(Dispatchers.IO) {
            db.favoriteDriversDao().isFavorite(driverId)
        }

    suspend fun toggleFavorite(driver: FavoriteDriverEntity) {
        withContext(Dispatchers.IO) {
            if (db.favoriteDriversDao().isFavorite(driver.driverId)) {
                db.favoriteDriversDao().deleteById(driver.driverId)
            } else {
                db.favoriteDriversDao().insert(driver)
            }
        }
    }
}