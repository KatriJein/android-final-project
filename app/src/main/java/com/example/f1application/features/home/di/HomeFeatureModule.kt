package com.example.f1application.features.home.di

import com.example.f1application.features.home.repository.HomeRepository
import com.example.f1application.features.home.viewModel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {

    single { HomeRepository(get()) }
    viewModel { HomeViewModel(get(), get()) }
}