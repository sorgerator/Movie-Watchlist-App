package com.example.movie_watchlist_app.model

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val description: String = "",
    val priority: String = "Medium",
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false,
    val imageResId: Int? = null
)