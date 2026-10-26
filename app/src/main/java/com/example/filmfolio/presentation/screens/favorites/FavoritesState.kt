package com.example.filmfolio.presentation.screens.favorites

import com.example.filmfolio.data.local.entity.Movie

sealed class FavoritesUiState {
    object Init : FavoritesUiState()
    object Loading : FavoritesUiState()
    data class Success(val favoriteMovies: List<Movie>) : FavoritesUiState()
    data class Error(val message: String?) : FavoritesUiState()
}

sealed class FavoritesNavigationEvent {
    data class NavigateToMovieDetails(val movieId: Long) : FavoritesNavigationEvent()
}
