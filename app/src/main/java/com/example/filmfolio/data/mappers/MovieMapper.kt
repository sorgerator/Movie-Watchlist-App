package com.example.filmfolio.data.mappers

import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.remote.CreateMovieDto
import com.example.filmfolio.data.remote.MovieDto
import com.example.filmfolio.data.remote.UpdateMovieDto

/**
 * Maps network DTO [MovieDto] to domain entity [Movie].
 */
fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        remoteId = id.toString(),
        title = title,
        description = description,
        genre = genre,
        priority = priority,
        isWatched = isWatched,
        isFavorite = isFavorite,
        releaseDate = releaseDate,
        posterUrl = posterUrl
    )
}

/**
 * Maps domain entity [Movie] to creation DTO [CreateMovieDto].
 */
fun Movie.toCreateDto(): CreateMovieDto {
    return CreateMovieDto(
        title = title,
        genre = genre,
        description = description,
        priority = priority,
        isWatched = isWatched,
        isFavorite = isFavorite,
        releaseDate = releaseDate,
        posterUrl = posterUrl,
        userId = remoteId ?: ""
    )
}

/**
 * Maps domain entity [Movie] to update DTO [UpdateMovieDto].
 */
fun Movie.toUpdateDto(): UpdateMovieDto {
    return UpdateMovieDto(
        title = title,
        genre = genre,
        description = description,
        priority = priority,
        isWatched = isWatched,
        isFavorite = isFavorite,
        releaseDate = releaseDate,
        posterUrl = posterUrl,
        userId = remoteId
    )
}
