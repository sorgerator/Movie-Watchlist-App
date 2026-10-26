package com.example.filmfolio.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_genre_cross_ref",
    primaryKeys = ["movieId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["id"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieId"), Index("genreId")]
)
data class MovieGenreCrossRef(
    val movieId: Long,
    val genreId: Long
)
