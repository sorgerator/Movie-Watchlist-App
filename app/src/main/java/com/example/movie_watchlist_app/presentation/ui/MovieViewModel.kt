package com.example.movie_watchlist_app.presentation.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.movie_watchlist_app.presentation.util.Movie

class MovieViewModel : ViewModel() {
    private val _movies = mutableStateListOf<Movie>()
    val movies: List<Movie> get() = _movies

    fun addMovie(title: String, genre: String) {
        val newId = (_movies.maxByOrNull { it.id }?.id ?: 0) + 1
        _movies.add(Movie(id = newId, title = title, genre = genre))
    }
}