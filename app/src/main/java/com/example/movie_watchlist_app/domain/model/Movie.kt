package com.example.movie_watchlist_app.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val description: String,
    val genre: String = "",
    val priority: String = "Medium",
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false,
    val releaseDate: String = "",
    val posterUrl: String = ""
)
