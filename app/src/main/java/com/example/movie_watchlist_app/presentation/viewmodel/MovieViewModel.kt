package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.movie_watchlist_app.model.Movie
import com.example.movie_watchlist_app.model.repository.SessionRepository
import com.example.movie_watchlist_app.model.repository.movie.MovieRepository
import com.example.movie_watchlist_app.model.repository.watchlist.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    val allMovies = movieRepository.getAllMovies().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    val watchlist = sessionRepository.loggedInUserId.flatMapLatest { userId ->
        if (userId != null) movieRepository.getWatchlistMovies(userId) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onGenreChange(genre: String) {
        _selectedGenre.value = genre
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    val filteredMovies = combine(allMovies, searchQuery, selectedGenre) { movies, query, genre ->
        movies.filter {
            (genre == "All" || it.genre == genre) &&
                    (query.isEmpty() || it.title.contains(query, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteMovies = allMovies.map { it.filter { movie -> movie.isFavorite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalMoviesCount = allMovies.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val watchedMoviesCount = allMovies.map { it.count { m -> m.isWatched } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val favoriteMoviesCount = favoriteMovies.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun toggleWatched(movieId: Int) {}
    fun toggleFavorite(movieId: Int) {}
    fun deleteMovie(movieId: Int) {}
    fun addMovie(title: String, genre: String, description: String, priority: String) {
        // TODO: Map to Movie domain model and insert
    }
}