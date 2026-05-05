package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AddQuestUiState {
    object Init : AddQuestUiState()
    object Loading : AddQuestUiState()
    object Success : AddQuestUiState()
}

class AddQuestViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AddQuestUiState>(AddQuestUiState.Init)
    val uiState: StateFlow<AddQuestUiState> = _uiState

    fun addQuest(isValid: Boolean) {
        if (isValid) {
            viewModelScope.launch {
                _uiState.value = AddQuestUiState.Loading
                saveQuest()
                _uiState.value = AddQuestUiState.Success
            }
        }
    }

    private suspend fun saveQuest() {
        delay(1000)
    }
}
