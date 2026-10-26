package com.example.filmfolio.data.local.entity

data class Movie(
    val id: Long = 0,
    val remoteId: String? = null,
    val title: String,
    val description: String,
    val genre: String = "",
    val priority: String = "Medium",
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false,
    val releaseDate: String = "",
    val posterUrl: String = ""
)
