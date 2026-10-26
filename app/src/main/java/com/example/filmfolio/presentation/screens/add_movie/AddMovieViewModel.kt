package com.example.filmfolio.presentation.screens.add_movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.repository.MovieRepository
import com.example.filmfolio.data.repository.SessionRepository
import com.example.filmfolio.data.repository.WatchlistRepository
import com.example.filmfolio.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddMovieNavigationEvent {
    object NavigateBack : AddMovieNavigationEvent()
}

sealed interface AddMovieUiState {
    object Init : AddMovieUiState
    object Loading : AddMovieUiState
    object Success : AddMovieUiState
    data class Error(val message: String) : AddMovieUiState
}

@HiltViewModel
class AddMovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sessionRepository: SessionRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddMovieUiState>(AddMovieUiState.Init)
    val uiState = _uiState.asStateFlow()


    private val _navigationEvents = Channel<AddMovieNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onAddMovie(
        title: String,
        description: String,
        genre: String,
        priority: String,
        releaseDate: String
    ) {
        viewModelScope.launch {
            _uiState.value = AddMovieUiState.Loading
            try {
                val movie = Movie(
                    title = title,
                    description = description,
                    genre = genre,
                    priority = priority,
                    releaseDate = releaseDate
                )
                
                // Insert locally first to get the generated local ID
                val generatedId = movieRepository.insertMovie(movie)
                val movieWithId = movie.copy(id = generatedId)

                // Add to watchlist for the logged-in user
                sessionRepository.loggedInUserId.first()?.let { userId ->
                    watchlistRepository.addToWatchlist(userId, generatedId, "Plan to Watch")
                }

                // Add to Firestore with matching ID
                val result = movieRepository.addMovieToFirestore(movieWithId)
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = AddMovieUiState.Success
                        _navigationEvents.send(AddMovieNavigationEvent.NavigateBack)
                    }
                    is Resource.Error -> {
                        _uiState.value = AddMovieUiState.Error(result.message ?: "Failed to add movie")
                    }
                    is Resource.Loading -> {
                        _uiState.value = AddMovieUiState.Loading
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = AddMovieUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            try {
                _navigationEvents.send(AddMovieNavigationEvent.NavigateBack)
            } catch (e: CancellationException) {
                throw e
            }
        }
    }
}
