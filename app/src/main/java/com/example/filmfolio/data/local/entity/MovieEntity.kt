package com.example.filmfolio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val genre: String,
    val description: String,
    val priority: String = "Medium",
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false,
    val releaseDate: String = "",
    val posterUrl: String = ""
)
