package com.example.movie_watchlist_app.domain.model

data class WatchlistItem(
    val id: Long,
    val userId: Long,
    val movie: Movie,
    val status: String,
    val addedAt: Long
)
