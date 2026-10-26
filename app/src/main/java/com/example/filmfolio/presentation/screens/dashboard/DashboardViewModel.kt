package com.example.filmfolio.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.repository.AuthRepository
import com.example.filmfolio.data.repository.MovieRepository
import com.example.filmfolio.data.repository.SessionRepository
import com.example.filmfolio.data.repository.UserRepository
import com.example.filmfolio.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigationEvents = Channel<DashboardNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    val uiState: StateFlow<DashboardUiState> = movieRepository.getMoviesFromFirestore()
        .map { resource ->
            when (resource) {
                is Resource.Success -> DashboardUiState.Success(resource.data ?: emptyList())
                is Resource.Error -> DashboardUiState.Error(resource.message ?: "Failed to load movies")
                is Resource.Loading -> DashboardUiState.Loading
            }
        }
        .onStart {
            // Optional: still seed local DB if needed
            movieRepository.seedDatabase()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState.Init
        )

    init {
        viewModelScope.launch {
            try {
                val firebaseUser = authRepository.getCurrentUser()
                val savedUserId = sessionRepository.loggedInUserId.first()
                if (firebaseUser == null && savedUserId == null) {
                    _navigationEvents.send(DashboardNavigationEvent.Logout)
                } else if (firebaseUser?.email != null && (savedUserId == null || userRepository.getUserById(savedUserId) == null)) {
                    val emailVal = firebaseUser.email!!
                    val existing = userRepository.getUserByEmail(emailVal)
                    val userId = existing?.id ?: userRepository.register(
                        firebaseUser.displayName ?: "User",
                        emailVal,
                        ""
                    )
                    sessionRepository.saveSession(userId)
                }
            } catch (e: Exception) {
                // Silent catch
            }
        }
    }

    fun onMovieClick(movieId: Long) {
        viewModelScope.launch {
            try {
                _navigationEvents.send(DashboardNavigationEvent.NavigateToMovieDetails(movieId))
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                sessionRepository.clearSession()
                _navigationEvents.send(DashboardNavigationEvent.Logout)
            } catch (e: CancellationException) {
                throw e
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
                // Silently handle error or log it if needed
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

    fun onDeleteMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                movie.remoteId?.let {
                    movieRepository.deleteMovieFromFirestore(it)
                } ?: movieRepository.deleteMovie(movie.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Silently handle error
            }
        }
    }
}
