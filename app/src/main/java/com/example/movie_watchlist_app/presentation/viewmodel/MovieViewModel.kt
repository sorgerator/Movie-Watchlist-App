package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.movie_watchlist_app.model.Movie
import com.example.movie_watchlist_app.model.HardcodedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MovieViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(HardcodedData.movies)
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    val filteredMovies = combine(
        _movies,
        _searchQuery,
        _selectedGenre
    ) {
        movies,
        query,
        genre ->
        movies.filter { movie ->
            (query.isEmpty() || movie.title.contains(query, ignoreCase = true) || movie.genre.contains(query, ignoreCase = true)) &&
            (genre == "All" || movie.genre == genre)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onGenreChange(genre: String) {
        _selectedGenre.value = genre
    }

    val favoriteMovies = _movies.map { movies ->
        movies.filter {
            it.isFavorite
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val watchedMoviesCount = _movies.map { movies ->
        movies.count {
            it.isWatched
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    val favoriteMoviesCount = _movies.map { movies ->
        movies.count {
            it.isFavorite
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    val totalMoviesCount = _movies.map { movies ->
        movies.size
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addMovie(
        title: String,
        genre: String,
        description: String,
        priority: String
    ) {
        val newId = (_movies.value.maxByOrNull {
            it.id
        }?.id ?: 0) + 1
        _movies.value = _movies.value + Movie(
            newId,
            title,
            genre,
            description,
            priority,
            false,
            false
        )
    }

    fun toggleWatched(movieId: Int) {
        _movies.value = _movies.value.map {
            if (it.id == movieId) it.copy(isWatched = !it.isWatched) else it
        }
    }

    fun toggleFavorite(movieId: Int) {
        _movies.value = _movies.value.map {
            if (it.id == movieId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun deleteMovie(movieId: Int) {
        _movies.value = _movies.value.filter {
            it.id != movieId
        }
    }
}