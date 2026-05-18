package com.example.movie_watchlist_app.ui.features.profile

import com.example.movie_watchlist_app.domain.model.User

sealed interface ProfileUiState {
    object Initial : ProfileUiState
    object Loading : ProfileUiState
    data class Success(val user: User) : ProfileUiState
    data class Error(val message: String?) : ProfileUiState
}

sealed interface ProfileNavigationEvent {
    object Logout : ProfileNavigationEvent
}
