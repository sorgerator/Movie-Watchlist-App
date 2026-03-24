package com.example.movie_watchlist_app.presentation.util

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val description: String = ""
)
