package com.example.f1application.features.drivers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.mappers.toDomain
import com.example.f1application.core.model.DriverResult
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.features.drivers.repository.DriversRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriverDetailViewModel(
    private val repository: DriversRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DriverDetailUiState>(DriverDetailUiState.Loading)
    val uiState: StateFlow<DriverDetailUiState> = _uiState.asStateFlow()

    fun retry(driverId: String) {
        loadDriverDetails(driverId)
    }

    fun loadDriverDetails(driverId: String) {
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
}

sealed class DriverDetailUiState {
    object Loading : DriverDetailUiState()
    data class Success(
        val driverStanding: DriverStanding,
        val results: List<DriverResult>
    ) : DriverDetailUiState()

    data class Error(val message: String) : DriverDetailUiState()
}