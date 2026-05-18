package com.example.movie_watchlist_app.ui.features.registration

sealed interface RegisterUiState {
    object Initial : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String?) : RegisterUiState
}

sealed interface RegisterNavigationEvent {
    object NavigateBackToLogin : RegisterNavigationEvent
}
