package com.example.filmfolio.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.repository.MovieRepository
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
            initialValue = FavoritesUiState.Init
        )

    fun onMovieClick(movieId: Long) {
        viewModelScope.launch {
            try {
                _navigationEvents.send(FavoritesNavigationEvent.NavigateToMovieDetails(movieId))
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun onToggleFavorite(movie: Movie) {
        viewModelScope.launch {
            try {
                movie.remoteId?.let {
                    movieRepository.updateFavoriteStatusInFirestore(it, !movie.isFavorite)
                }
                movieRepository.updateFavoriteStatus(movie.id, !movie.isFavorite)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Silently handle error
            }
        }
    }

    fun onToggleWatched(movie: Movie) {
        viewModelScope.launch {
            try {
                movie.remoteId?.let {
                    movieRepository.updateWatchedStatusInFirestore(it, !movie.isWatched)
                }
                movieRepository.updateWatchedStatus(movie.id, !movie.isWatched)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Silently handle error
            }
        }
    }
}
