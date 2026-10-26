package com.example.filmfolio.presentation.screens.details

import com.example.filmfolio.data.local.entity.Movie

sealed interface MovieDetailsUiState {
    object Init : MovieDetailsUiState
    object Loading : MovieDetailsUiState
    data class Success(
        val movie: Movie,
        val isInWatchlist: Boolean,
        val currentStatus: String?
    ) : MovieDetailsUiState
    data class Error(val message: String?) : MovieDetailsUiState
}

sealed interface MovieDetailsNavigationEvent {
    object NavigateBack : MovieDetailsNavigationEvent
}
