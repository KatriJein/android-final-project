package com.example.f1application.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.f1application.core.api.F1ApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
//        OkHttpClient.Builder()
//            .addInterceptor(ChuckerInterceptor(get()))
//            .build()

        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(get()))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(RetryInterceptor())
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

class RetryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0
        while (!response.isSuccessful && tryCount < 3) {
            tryCount++
            response.close()
            response = chain.proceed(request)
        }
        return response
    }
}

