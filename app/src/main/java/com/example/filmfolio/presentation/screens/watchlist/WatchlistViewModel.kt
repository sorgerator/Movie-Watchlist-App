package com.example.filmfolio.presentation.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.repository.SessionRepository
import com.example.filmfolio.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.repository.MovieRepository

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
    private val movieRepository: MovieRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _navigationEvents = Channel<WatchlistNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WatchlistUiState> = sessionRepository.loggedInUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                watchlistRepository.getWatchlistForUser(userId).map { list ->
                    WatchlistUiState.Success(list)
                }
            } else {
                flowOf(WatchlistUiState.Error("User not logged in"))
            }
        }
        .onStart { emit(WatchlistUiState.Loading) }
        .catch { e -> 
            if (e is CancellationException) throw e
            emit(WatchlistUiState.Error(e.message ?: "Failed to load watchlist")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WatchlistUiState.Init
        )

    fun onMovieClick(movieId: Long) {
        viewModelScope.launch {
            try {
                _navigationEvents.send(WatchlistNavigationEvent.NavigateToMovieDetails(movieId))
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun onRemoveClick(movieId: Long) {
        viewModelScope.launch {
            try {
                sessionRepository.loggedInUserId.first()?.let { userId ->
                    watchlistRepository.removeFromWatchlist(userId, movieId)
                }
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
}
