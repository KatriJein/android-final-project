package com.example.f1application.features.home.di

import com.example.f1application.features.home.repository.HomeRepository
import com.example.f1application.features.home.viewModel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {

//    factory { DriversResponseToEntityMapper() }
    single { HomeRepository(get()) }
//    single { FavoriteDriversRepository(get()) }

//    single { DriversInteractor(get(), get(),get()) }
//    single { BadgeCacheManager() }

    viewModel { HomeViewModel(get(), get()) }
//    viewModel { params ->
//        DriversDetailsViewModel(
//            topLevelBackStack = get(),
//            interactor = get(),
//            driverId = params.get(),
//            initialPoints = params.getOrNull(),
//            initialPosition = params.getOrNull(),
//            initialWins = params.getOrNull()
//        )
//    }
}