package com.example.f1application.features.races.di

import com.example.f1application.features.races.repository.RacesRepository
import com.example.f1application.features.races.viewModel.RacesListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val racesFeatureModule = module {

    single { RacesRepository(get()) }
    viewModel { RacesListViewModel(get(), get()) }

}