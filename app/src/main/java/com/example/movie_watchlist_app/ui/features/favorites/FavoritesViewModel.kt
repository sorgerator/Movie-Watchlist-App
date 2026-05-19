package com.example.movie_watchlist_app.ui.features.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.domain.model.Movie
import com.example.movie_watchlist_app.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _navigationEvents = Channel<FavoritesNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    val uiState: StateFlow<FavoritesUiState> = movieRepository.getAllMovies()
        .map { movies -> 
            val favorites = movies.filter { it.isFavorite }
            FavoritesUiState.Success(favorites) as FavoritesUiState 
        }
        .onStart { emit(FavoritesUiState.Loading) }
        .catch { e -> 
            if (e is CancellationException) throw e
            emit(FavoritesUiState.Error(e.message ?: "Failed to load favorites")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState.Initial
        )

    fun onMovieClick(movieId: Long) {
        viewModelScope.launch {
            _navigationEvents.send(FavoritesNavigationEvent.NavigateToMovieDetails(movieId))
        }
    }

    fun onToggleFavorite(movie: Movie) {
        viewModelScope.launch {
            movieRepository.updateFavoriteStatus(movie.id, !movie.isFavorite)
        }
    }

    fun onToggleWatched(movie: Movie) {
        viewModelScope.launch {
            movieRepository.updateWatchedStatus(movie.id, !movie.isWatched)
        }
    }
}
