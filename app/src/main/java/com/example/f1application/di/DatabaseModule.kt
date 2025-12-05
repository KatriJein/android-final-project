package com.example.f1application.di

import androidx.room.Room
import com.example.f1application.core.db.AppDatabase
import com.example.f1application.core.db.UserDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { UserDataStore(androidContext()) }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "f1_db")
            .build()
    }
    single { get<AppDatabase>().favoriteDriversDao() }
}