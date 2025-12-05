package com.example.f1application.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.f1application.core.api.F1ApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(get()))
            .build()
    }

    single {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
        Retrofit.Builder()
            .baseUrl("https://f1api.dev/api/")
            .client(get())
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

    single { get<Retrofit>().create(F1ApiService::class.java) }
}


