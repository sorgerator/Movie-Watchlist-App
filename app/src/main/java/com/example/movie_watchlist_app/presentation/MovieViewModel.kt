package com.example.movie_watchlist_app.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.movie_watchlist_app.presentation.util.Movie

class MovieViewModel : ViewModel() {
    private val _movies = mutableStateListOf<Movie>()
    val movies: List<Movie> get() = _movies

    fun addMovie(title: String, genre: String, description: String, priority: String) {
        val newId = (_movies.maxByOrNull { it.id }?.id ?: 0) + 1
        _movies.add(Movie(newId, title, genre, description, priority, false, false))
    }

    fun toggleWatched(movieId: Int, isWatched: Boolean) {
        val index = _movies.indexOfFirst { it.id == movieId }
        if (index != -1) {
            _movies[index] = _movies[index].copy(isWatched = isWatched)
        }
    }

    fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        val index = _movies.indexOfFirst { it.id == movieId }
        if (index != -1) {
            _movies[index] = _movies[index].copy(isFavorite = isFavorite)
        }
    }

    fun deleteMovie(movieId: Int) {
        _movies.removeAll { it.id == movieId }
    }
}