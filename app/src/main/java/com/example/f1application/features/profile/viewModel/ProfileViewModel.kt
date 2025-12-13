package com.example.f1application.features.profile.viewModel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.db.FavoriteDriverEntity
import com.example.f1application.core.model.ProfileEntity
import com.example.f1application.core.navigation.DriverDetails
import com.example.f1application.core.navigation.Drivers
import com.example.f1application.core.navigation.EditProfile
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val repository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileViewState>(ProfileViewState.Loading)
    val uiState: StateFlow<ProfileViewState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.observeProfile().collect { profile ->
                val current = _uiState.value
                val updatedProfile = profile.copy(
                    photoUri = if (profile.photoUri.isNotEmpty())
                        profile.photoUri.toUri().toString()
                    else
                        ""
                )
                updateState(current, updatedProfile)
            }
        }

        viewModelScope.launch {
            try {
                repository.getFavoriteDrivers().collect { favorites ->
                    val current = _uiState.value
                    val updatedFavorites = favorites
                    updateStateWithFavorites(current, updatedFavorites)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun updateState(current: ProfileViewState, profile: ProfileEntity) {
        when (current) {
            is ProfileViewState.Loading -> {
                _uiState.value = ProfileViewState.Success(
                    favouritesDrivers = emptyList(),
                    profile = profile
                )
            }

            is ProfileViewState.Success -> {
                _uiState.value = current.copy(profile = profile)
            }

            is ProfileViewState.Error -> {
                _uiState.value = ProfileViewState.Success(
                    favouritesDrivers = emptyList(),
                    profile = profile
                )
            }
        }
    }

    private fun updateStateWithFavorites(
        current: ProfileViewState,
        favorites: List<FavoriteDriverEntity>
    ) {
        when (current) {
            is ProfileViewState.Loading -> {
                _uiState.value = ProfileViewState.Success(
                    favouritesDrivers = favorites,
                    profile = ProfileEntity("", "")
                )
            }

            is ProfileViewState.Success -> {
                _uiState.value = current.copy(favouritesDrivers = favorites)
            }

            is ProfileViewState.Error -> {
                _uiState.value = ProfileViewState.Success(
                    favouritesDrivers = favorites,
                    profile = ProfileEntity("", "")
                )
            }
        }
    }

    fun onEditProfile() {
        topLevelBackStack.add(EditProfile)
    }

    fun onSearchDrivers() {
        topLevelBackStack.addTopLevel(Drivers)
    }

    fun onDriverClick(driverId: String) {
        topLevelBackStack.add(
            DriverDetails(driverId = driverId)
        )
    }
}

sealed class ProfileViewState {
    object Loading : ProfileViewState()
    data class Success(
        val favouritesDrivers: List<FavoriteDriverEntity>,
        val profile: ProfileEntity
    ) : ProfileViewState()

    data class Error(val message: String) : ProfileViewState()
}