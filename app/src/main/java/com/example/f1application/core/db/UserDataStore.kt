package com.example.f1application.core.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class UserDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")
    private val datastore = context.dataStore

    val userNameFlow = datastore.data.map { it[PreferencesKeys.USER_NAME] ?: "" }
    val userPhotoFlow = datastore.data.map { it[PreferencesKeys.USER_PHOTO] ?: "" }

    suspend fun saveUserName(name: String) {
        datastore.edit { it[PreferencesKeys.USER_NAME] = name }
    }

    suspend fun saveUserPhoto(url: String) {
        datastore.edit { it[PreferencesKeys.USER_PHOTO] = url }
    }

    object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PHOTO = stringPreferencesKey("user_photo")
    }
}