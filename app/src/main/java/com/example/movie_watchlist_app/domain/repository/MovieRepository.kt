package com.example.movie_watchlist_app.domain.repository

import com.example.movie_watchlist_app.domain.model.Genre
import com.example.movie_watchlist_app.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun seedDatabase()
    suspend fun insertMovie(movie: Movie): Long
    suspend fun insertGenre(genre: Genre): Long
    suspend fun addGenreToMovie(movieId: Long, genreId: Long)
    fun getAllMovies(): Flow<List<Movie>>
    fun getAllGenres(): Flow<List<Genre>>
    fun getGenresForMovie(movieId: Long): Flow<List<Genre>>
    suspend fun getMovieById(movieId: Long): Movie?
    fun getMovieByIdFlow(movieId: Long): Flow<Movie?>
    suspend fun updateWatchedStatus(movieId: Long, isWatched: Boolean)
    suspend fun updateFavoriteStatus(movieId: Long, isFavorite: Boolean)
    suspend fun deleteMovie(movieId: Long)
}
