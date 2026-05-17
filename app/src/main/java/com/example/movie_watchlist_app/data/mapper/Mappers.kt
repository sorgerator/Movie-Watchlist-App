package com.example.movie_watchlist_app.data.mapper

import com.example.movie_watchlist_app.data.local.entity.*
import com.example.movie_watchlist_app.domain.model.*

fun UserEntity.toDomain() = User(
    id = id,
    fullName = fullName,
    email = email,
    createdAt = createdAt
)

fun User.toEntity(password: String = "") = UserEntity(
    id = id,
    fullName = fullName,
    email = email,
    password = password,
    createdAt = createdAt
)

fun MovieEntity.toDomain(genres: List<Genre> = emptyList()) = Movie(
    id = id,
    title = title,
    genre = if (genres.isNotEmpty()) genres.joinToString(", ") { it.name } else genre,
    description = description,
    priority = priority,
    isWatched = isWatched,
    isFavorite = isFavorite,
    releaseDate = releaseDate,
    posterUrl = posterUrl
)

fun Movie.toEntity() = MovieEntity(
    id = id,
    title = title,
    genre = genre,
    description = description,
    priority = priority,
    isWatched = isWatched,
    isFavorite = isFavorite,
    releaseDate = releaseDate,
    posterUrl = posterUrl
)

fun GenreEntity.toDomain() = Genre(
    id = id,
    name = name
)

fun Genre.toEntity() = GenreEntity(
    id = id,
    name = name
)

fun WatchlistItemEntity.toDomain(movie: Movie) = WatchlistItem(
    id = id,
    userId = userId,
    movie = movie,
    status = status,
    addedAt = addedAt
)
