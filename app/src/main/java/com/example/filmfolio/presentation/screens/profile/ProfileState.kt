package com.example.filmfolio.presentation.screens.profile

import com.example.filmfolio.data.local.entity.User

sealed interface ProfileUiState {
    object Init : ProfileUiState
    object Loading : ProfileUiState
    data class Success(val user: User) : ProfileUiState
    data class Error(val message: String?) : ProfileUiState
}

sealed interface ProfileNavigationEvent {
    object Logout : ProfileNavigationEvent
}
