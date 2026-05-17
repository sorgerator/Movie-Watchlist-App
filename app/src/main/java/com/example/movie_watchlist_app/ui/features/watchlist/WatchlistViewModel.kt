package com.example.movie_watchlist_app.ui.features.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.domain.repository.SessionRepository
import com.example.movie_watchlist_app.domain.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
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
            initialValue = WatchlistUiState.Initial
        )

    fun onMovieClick(movieId: Long) {
        viewModelScope.launch {
            _navigationEvents.send(WatchlistNavigationEvent.NavigateToMovieDetails(movieId))
        }
    }

    fun onRemoveClick(movieId: Long) {
        viewModelScope.launch {
            try {
                sessionRepository.loggedInUserId.first()?.let { userId ->
                    watchlistRepository.removeFromWatchlist(userId, movieId)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                // Error handled in UI if needed
            }
        }
    }
}
