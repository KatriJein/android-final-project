package com.example.f1application.features.drivers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.db.FavoriteDriverEntity
import com.example.f1application.core.mappers.toDomain
import com.example.f1application.core.model.DriverResult
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.features.drivers.repository.DriversRepository
import com.example.f1application.features.drivers.repository.FavoriteDriversRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriverDetailViewModel(
    private val driverId: String,
    private val repository: DriversRepository,
    private val favoriteRepository: FavoriteDriversRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DriverDetailUiState>(DriverDetailUiState.Loading)
    val uiState: StateFlow<DriverDetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        loadDriverDetails()
        checkIsFavorite()
    }

    fun retry() {
        loadDriverDetails()
        checkIsFavorite()
    }

    fun loadDriverDetails() {
        viewModelScope.launch {
            _uiState.value = DriverDetailUiState.Loading
            try {
                val driverDetails = repository.getDriverDetails(driverId)
                val driverStanding = repository.getDriverStanding(driverId)
                val results = driverDetails.results.map { it.toDomain() }
                _uiState.value = DriverDetailUiState.Success(driverStanding, results)
            } catch (e: Exception) {
                _uiState.value = DriverDetailUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun checkIsFavorite() {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavorite(driverId)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val standing = (uiState.value as? DriverDetailUiState.Success)?.driverStanding ?: return@launch
            val entity = FavoriteDriverEntity(
                driverId = standing.driver.id,
                firstName = standing.driver.firstName,
                lastName = standing.driver.lastName,
                imageUrl = standing.driver.imageUrl,
                teamId = standing.team.id,
                teamName = standing.team.name
            )
            favoriteRepository.toggleFavorite(entity)
            _isFavorite.value = !_isFavorite.value
        }
    }
}

sealed class DriverDetailUiState {
    object Loading : DriverDetailUiState()
    data class Success(
        val driverStanding: DriverStanding,
        val results: List<DriverResult>
    ) : DriverDetailUiState()

    data class Error(val message: String) : DriverDetailUiState()
}