package com.example.f1application.features.profile.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.f1application.core.db.AppDatabase
import com.example.f1application.core.db.FavoriteDriverEntity
import com.example.f1application.core.model.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val dataStore: DataStore<Preferences>,
    private val db: AppDatabase
) {

    fun observeProfile(): Flow<ProfileEntity> {
        return dataStore.data.map { preferences ->
            ProfileEntity(
                fullName = preferences[FULL_NAME_KEY] ?: "",
                photoUri = preferences[PHOTO_URI_KEY] ?: "",
            )
        }
    }

    suspend fun setProfile(
        fullName: String,
        photoUri: String,
    ) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[FULL_NAME_KEY] = fullName
            preferences[PHOTO_URI_KEY] = photoUri
        }
    }

    fun getFavoriteDrivers(): Flow<List<FavoriteDriverEntity>> =
        db.favoriteDriversDao().getAllFavorites()

    companion object {
        private val FULL_NAME_KEY = stringPreferencesKey("full_name_profile")
        private val PHOTO_URI_KEY = stringPreferencesKey("photo_uri_profile")
    }
}