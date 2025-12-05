package com.example.f1application.di

import com.example.f1application.core.navigation.Home
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Home) }

}