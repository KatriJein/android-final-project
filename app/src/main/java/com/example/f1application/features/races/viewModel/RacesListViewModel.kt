package com.example.f1application.features.races.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.model.Race
import com.example.f1application.core.navigation.RaceDetails
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.races.repository.RacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RacesListViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val repository: RacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RacesListUiState>(RacesListUiState.Loading)
    val uiState: StateFlow<RacesListUiState> = _uiState.asStateFlow()

    private var originalRaces: List<Race> = emptyList()
    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending.asStateFlow()

    init {
        loadRaces()
    }

    private fun loadRaces() {
        viewModelScope.launch {
            _uiState.value = RacesListUiState.Loading
            try {
                val races = repository.getAllRaces()
                originalRaces = races
                val sortedRaces = sortRaces(races, _isAscending.value)
                _uiState.value = RacesListUiState.Success(sortedRaces)
            } catch (e: Exception) {
                _uiState.value = RacesListUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun toggleSortOrder() {
        _isAscending.update { !it }
        val currentRaces = (uiState.value as? RacesListUiState.Success)?.races ?: originalRaces
        val sortedRaces = sortRaces(currentRaces, _isAscending.value)
        _uiState.value = RacesListUiState.Success(sortedRaces)
    }

    private fun sortRaces(races: List<Race>, ascending: Boolean): List<Race> {
        return if (ascending) {
            races.sortedBy { race ->
                parseDateSafe(race.date)
            }
        } else {
            races.sortedByDescending { race ->
                parseDateSafe(race.date)
            }
        }
    }

    private fun parseDateSafe(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        } catch (e: Exception) {
            null
        }
    }

    fun onRaceClick(race: Race) {
        topLevelBackStack.add(RaceDetails(race))
    }
}

sealed class RacesListUiState {
    object Loading : RacesListUiState()
    data class Success(val races: List<Race>) : RacesListUiState()
    data class Error(val message: String) : RacesListUiState()
}