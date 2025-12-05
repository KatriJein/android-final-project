package com.example.f1application.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.model.Race
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

    fun onRaceClick(race: Race) {
        topLevelBackStack.add(RaceDetails(race))
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                val nextRace = repository.getNextRace()
//                val topDrivers = repository.getTopDrivers()
//                val topTeams = repository.getTopTeams()
                _uiState.value = HomeUiState.Success(nextRace)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val nextRace: Race,
//        val topDrivers: List<Driver>,
//        val topTeams: List<Team>
    ) : HomeUiState()

    data class Error(val message: String) : HomeUiState()
}