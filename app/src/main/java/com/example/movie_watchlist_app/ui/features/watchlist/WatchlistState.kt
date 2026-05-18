package com.example.movie_watchlist_app.ui.features.watchlist

import com.example.movie_watchlist_app.domain.model.WatchlistItem

sealed interface WatchlistUiState {
    object Initial : WatchlistUiState
    object Loading : WatchlistUiState
    data class Success(val watchlist: List<WatchlistItem>) : WatchlistUiState
    data class Error(val message: String?) : WatchlistUiState
}

sealed interface WatchlistNavigationEvent {
    data class NavigateToMovieDetails(val movieId: Long) : WatchlistNavigationEvent
    object NavigateBack : WatchlistNavigationEvent
}
