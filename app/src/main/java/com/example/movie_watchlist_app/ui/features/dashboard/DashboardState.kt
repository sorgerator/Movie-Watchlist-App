package com.example.movie_watchlist_app.ui.features.dashboard

import com.example.movie_watchlist_app.domain.model.Movie

sealed interface DashboardUiState {
    object Initial : DashboardUiState
    object Loading : DashboardUiState
    data class Success(val movies: List<Movie>) : DashboardUiState
    data class Error(val message: String?) : DashboardUiState
}

sealed interface DashboardNavigationEvent {
    data class NavigateToMovieDetails(val movieId: Long) : DashboardNavigationEvent
    object NavigateToProfile : DashboardNavigationEvent
    object Logout : DashboardNavigationEvent
}
