package com.example.movie_watchlist_app.ui.features.details

import com.example.movie_watchlist_app.domain.model.Movie

sealed interface MovieDetailsUiState {
    object Initial : MovieDetailsUiState
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
