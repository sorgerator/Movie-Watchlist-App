package com.example.filmfolio.data.local.entity

data class WatchlistItem(
    val id: Long,
    val userId: Long,
    val movie: Movie,
    val status: String,
    val addedAt: Long
)
