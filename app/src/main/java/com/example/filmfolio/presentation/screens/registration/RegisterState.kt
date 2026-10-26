package com.example.filmfolio.presentation.screens.registration

sealed interface RegisterUiState {
    object Init : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String?) : RegisterUiState
}

sealed interface RegisterNavigationEvent {
    object NavigateBackToLogin : RegisterNavigationEvent
}
