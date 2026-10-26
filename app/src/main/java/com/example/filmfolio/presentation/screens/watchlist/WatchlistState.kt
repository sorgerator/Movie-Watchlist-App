package com.example.filmfolio.presentation.screens.watchlist

import com.example.filmfolio.data.local.entity.WatchlistItem

sealed interface WatchlistUiState {
    object Init : WatchlistUiState
    object Loading : WatchlistUiState
    data class Success(val watchlist: List<WatchlistItem>) : WatchlistUiState
    data class Error(val message: String?) : WatchlistUiState
}

sealed interface WatchlistNavigationEvent {
    data class NavigateToMovieDetails(val movieId: Long) : WatchlistNavigationEvent
    object NavigateBack : WatchlistNavigationEvent
}
