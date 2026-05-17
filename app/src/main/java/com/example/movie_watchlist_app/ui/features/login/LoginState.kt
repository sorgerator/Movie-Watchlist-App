package com.example.movie_watchlist_app.ui.features.login

sealed interface LoginUiState {
    object Initial : LoginUiState
    object Loading : LoginUiState
    data class Success(val fullName: String) : LoginUiState
    data class Error(val message: String?) : LoginUiState
}

sealed interface LoginNavigationEvent {
    object NavigateToHome : LoginNavigationEvent
    object NavigateToRegister : LoginNavigationEvent
}
