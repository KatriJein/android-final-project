package com.example.f1application

import android.app.Application
import com.example.f1application.di.dbModule
import com.example.f1application.di.mainModule
import com.example.f1application.di.networkModule
import com.example.f1application.features.home.di.homeFeatureModule
import com.example.f1application.features.races.di.racesFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, networkModule, dbModule, homeFeatureModule, racesFeatureModule)
        }
    }
}