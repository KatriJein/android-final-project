package com.example.f1application.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.model.ConstructorStanding
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.core.model.Race
import com.example.f1application.core.navigation.DriverDetails
import com.example.f1application.core.navigation.RaceDetails
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.home.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun retry() {
        loadHomeData()
    }

    fun onDriverClick(driverId: String) {
        topLevelBackStack.add(DriverDetails(driverId))
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val topDrivers = repository.getTopDrivers(3)
                val topTeams = repository.getTopTeams(3)
                _uiState.value = HomeUiState.Success(topDrivers, topTeams)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val topDrivers: List<DriverStanding>,
        val topTeams: List<ConstructorStanding>
    ) : HomeUiState()

    data class Error(val message: String) : HomeUiState()
}