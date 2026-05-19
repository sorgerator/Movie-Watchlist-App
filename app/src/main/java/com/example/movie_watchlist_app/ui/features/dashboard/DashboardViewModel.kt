package com.example.movie_watchlist_app.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.domain.model.Movie
import com.example.movie_watchlist_app.domain.repository.MovieRepository
import com.example.movie_watchlist_app.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _navigationEvents = Channel<DashboardNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    val uiState: StateFlow<DashboardUiState> = movieRepository.getAllMovies()
        .map { movies -> DashboardUiState.Success(movies) as DashboardUiState }
        .onStart { 
            emit(DashboardUiState.Loading) 
            movieRepository.seedDatabase()
        }
        .catch { e -> 
            if (e is CancellationException) throw e
            emit(DashboardUiState.Error(e.message ?: "Failed to load movies")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState.Initial
        )

    fun onMovieClick(movieId: Long) {
        viewModelScope.launch {
            _navigationEvents.send(DashboardNavigationEvent.NavigateToMovieDetails(movieId))
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            sessionRepository.clearSession()
            _navigationEvents.send(DashboardNavigationEvent.Logout)
        }
    }

    fun onToggleWatched(movie: Movie) {
        viewModelScope.launch {
            movieRepository.updateWatchedStatus(movie.id, !movie.isWatched)
        }
    }

    fun onToggleFavorite(movie: Movie) {
        viewModelScope.launch {
            movieRepository.updateFavoriteStatus(movie.id, !movie.isFavorite)
        }
    }

    fun onDeleteMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movie.id)
        }
    }
}
