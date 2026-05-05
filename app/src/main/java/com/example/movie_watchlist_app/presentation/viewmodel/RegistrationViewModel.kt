package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegistrationUiState {
    object Init : RegistrationUiState()
    object Loading : RegistrationUiState()
    object Success : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}

class RegistrationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Init)
    val uiState: StateFlow<RegistrationUiState> = _uiState

    fun onRegisterClick() {
        viewModelScope.launch {
            _uiState.value = RegistrationUiState.Loading
            delay(1500)
            _uiState.value = RegistrationUiState.Success
        }
    }

    fun resetUiState() {
        _uiState.value = RegistrationUiState.Init
    }
}
