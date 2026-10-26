package com.example.filmfolio.presentation.screens.login

sealed interface LoginUiState {
    object Init : LoginUiState
    object Loading : LoginUiState
    data class Success(val fullName: String) : LoginUiState
    data class Error(val message: String?) : LoginUiState
}

sealed interface LoginNavigationEvent {
    object NavigateToHome : LoginNavigationEvent
    object NavigateToRegister : LoginNavigationEvent
}
