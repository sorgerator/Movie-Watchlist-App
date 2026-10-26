package com.example.filmfolio.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.repository.MovieRepository
import com.example.filmfolio.data.repository.SessionRepository
import com.example.filmfolio.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _movieId = MutableStateFlow<Long?>(null)

    private val _navigationEvents = Channel<MovieDetailsNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MovieDetailsUiState> = _movieId
        .filterNotNull()
        .flatMapLatest { movieId ->
            combine(
                movieRepository.getMovieByIdFlow(movieId),
                sessionRepository.loggedInUserId,
                { movie, userId -> movie to userId }
            ).flatMapLatest { (movie, userId) ->
                if (movie != null && userId != null) {
                    watchlistRepository.getWatchlistForUser(userId).map { watchlist ->
                        val item = watchlist.find { it.movie.id == movieId }
                        MovieDetailsUiState.Success(
                            movie = movie,
                            isInWatchlist = item != null,
                            currentStatus = item?.status
                        )
                    }
                } else {
                    flowOf(MovieDetailsUiState.Error("Data not found"))
                }
            }
        }
        .onStart { emit(MovieDetailsUiState.Loading) }
        .catch { e -> 
            if (e is CancellationException) throw e
            emit(MovieDetailsUiState.Error(e.message ?: "An error occurred")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieDetailsUiState.Init
        )

    fun loadMovieDetails(movieId: Long) {
        _movieId.value = movieId
    }

    fun onAddToWatchlist(movieId: Long, status: String) {
        viewModelScope.launch {
            try {
                sessionRepository.loggedInUserId.first()?.let { userId ->
                    watchlistRepository.addToWatchlist(userId, movieId, status)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                // Handle error
            }
        }
    }

    fun onUpdateStatus(movieId: Long, status: String) {
        viewModelScope.launch {
            try {
                sessionRepository.loggedInUserId.first()?.let { userId ->
                    watchlistRepository.updateStatus(userId, movieId, status)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                // Handle error
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            try {
                _navigationEvents.send(MovieDetailsNavigationEvent.NavigateBack)
            } catch (e: CancellationException) {
                throw e
            }
        }
    }
}
