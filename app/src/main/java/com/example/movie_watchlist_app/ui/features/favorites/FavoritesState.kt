package com.example.movie_watchlist_app.ui.features.favorites

import com.example.movie_watchlist_app.domain.model.Movie

sealed class FavoritesUiState {
    object Initial : FavoritesUiState()
    object Loading : FavoritesUiState()
    data class Success(val favoriteMovies: List<Movie>) : FavoritesUiState()
    data class Error(val message: String?) : FavoritesUiState()
}

sealed class FavoritesNavigationEvent {
    data class NavigateToMovieDetails(val movieId: Long) : FavoritesNavigationEvent()
}
