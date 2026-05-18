package com.example.movie_watchlist_app.domain.model

data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val createdAt: Long
)
