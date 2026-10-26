package com.example.filmfolio.presentation.screens.dashboard

import com.example.filmfolio.data.local.entity.Movie

sealed interface DashboardUiState {
    object Init : DashboardUiState
    object Loading : DashboardUiState
    data class Success(val movies: List<Movie>) : DashboardUiState
    data class Error(val message: String?) : DashboardUiState
}

sealed interface DashboardNavigationEvent {
    data class NavigateToMovieDetails(val movieId: Long) : DashboardNavigationEvent
    object NavigateToProfile : DashboardNavigationEvent
    object Logout : DashboardNavigationEvent
}
