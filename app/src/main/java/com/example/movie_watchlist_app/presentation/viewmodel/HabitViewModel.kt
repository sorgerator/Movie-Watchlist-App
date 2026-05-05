package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.model.HabitModel
import com.example.movie_watchlist_app.model.repository.FakeHabitRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HabitUiState {
    object Init : HabitUiState()
    object Loading : HabitUiState()
    data class Success(val habits: List<HabitModel>) : HabitUiState()
    data class Error(val message: String) : HabitUiState()
}

class HabitViewModel(private val repository: FakeHabitRepository = FakeHabitRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<HabitUiState>(HabitUiState.Init)
    val uiState: StateFlow<HabitUiState> = _uiState

    fun loadHabits(shouldFail: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = HabitUiState.Loading
            try {
                val habits = repository.getHabits(shouldFail)
                _uiState.value = HabitUiState.Success(habits)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = HabitUiState.Error(e.message ?: "Unknown error")
            } catch (e: Exception) {
                _uiState.value = HabitUiState.Error("An unexpected error occurred")
            }
        }
    }

    fun resetUiState() {
        loadHabits()
    }
}
