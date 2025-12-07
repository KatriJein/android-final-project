package com.example.f1application.features.drivers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.core.navigation.DriverDetails
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.drivers.repository.DriversRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DriversListViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val repository: DriversRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DriversListUiState>(DriversListUiState.Loading)
    val uiState: StateFlow<DriversListUiState> = _uiState.asStateFlow()

    private var originalStandings: List<DriverStanding> = emptyList()

    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending.asStateFlow()

    init {
        loadDrivers()
    }

    fun retry() {
        loadDrivers()
    }

    private fun loadDrivers() {
        viewModelScope.launch {
            _uiState.value = DriversListUiState.Loading
            try {
                val standings = repository.getDriversChampionship()
                originalStandings = standings
                val sortedStandings = sortStandings(standings, _isAscending.value)
                _uiState.value = DriversListUiState.Success(sortedStandings)
            } catch (e: Exception) {
                _uiState.value = DriversListUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun toggleSortOrder() {
        _isAscending.update { !it }
        val currentStandings =
            (uiState.value as? DriversListUiState.Success)?.standings ?: originalStandings
        val sortedStandings = sortStandings(currentStandings, _isAscending.value)
        _uiState.value = DriversListUiState.Success(sortedStandings)
    }

    private fun sortStandings(
        standings: List<DriverStanding>,
        ascending: Boolean
    ): List<DriverStanding> {
        return standings.sortedBy { if (ascending) it.position else -it.position }
    }

    fun onDriverClick(standing: DriverStanding) {
        topLevelBackStack.add(DriverDetails(standing.driver.id))
    }
}

sealed class DriversListUiState {
    object Loading : DriversListUiState()
    data class Success(val standings: List<DriverStanding>) : DriversListUiState()
    data class Error(val message: String) : DriversListUiState()
}