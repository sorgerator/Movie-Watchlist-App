package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Init : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(1500)
            _uiState.value = LoginUiState.Success
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Init
    }
}
