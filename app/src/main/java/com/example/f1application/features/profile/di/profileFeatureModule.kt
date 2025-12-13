package com.example.f1application.features.profile.di

import com.example.f1application.features.profile.repository.ProfileRepository
import com.example.f1application.features.profile.viewModel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileFeatureModule = module {

    single { ProfileRepository(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }

}