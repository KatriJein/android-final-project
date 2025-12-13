package com.example.f1application.features.drivers.di

import com.example.f1application.features.drivers.repository.DriversRepository
import com.example.f1application.features.drivers.repository.FavoriteDriversRepository
import com.example.f1application.features.drivers.viewModel.DriverDetailViewModel
import com.example.f1application.features.drivers.viewModel.DriversListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val driversFeatureModule = module {

    single { DriversRepository(get()) }
    single { FavoriteDriversRepository(get()) }
    viewModel { DriversListViewModel(get(), get()) }
    viewModel { (driverId: String) ->
        DriverDetailViewModel(
            driverId = driverId,
            repository = get(),
            favoriteRepository = get()
        )
    }

}