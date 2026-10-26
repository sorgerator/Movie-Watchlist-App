package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.entity.Genre
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.remote.CreateMovieDto
import com.example.filmfolio.data.remote.MovieDto
import com.example.filmfolio.data.remote.UpdateMovieDto
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

    // Firestore methods
    fun getMoviesFromFirestore(): Flow<com.example.filmfolio.util.Resource<List<Movie>>>
    suspend fun addMovieToFirestore(movie: Movie): com.example.filmfolio.util.Resource<Unit>
    suspend fun updateWatchedStatusInFirestore(remoteId: String, isWatched: Boolean): com.example.filmfolio.util.Resource<Unit>
    suspend fun updateFavoriteStatusInFirestore(remoteId: String, isFavorite: Boolean): com.example.filmfolio.util.Resource<Unit>
    suspend fun deleteMovieFromFirestore(remoteId: String): com.example.filmfolio.util.Resource<Unit>

    // Retrofit network methods
    suspend fun getNetworkWatchlist(): com.example.filmfolio.util.Resource<List<Movie>>
    suspend fun createNetworkMovie(movie: Movie): com.example.filmfolio.util.Resource<Movie>
    suspend fun updateNetworkMovie(id: String, movie: Movie): com.example.filmfolio.util.Resource<Movie>
    suspend fun deleteNetworkMovie(id: String): com.example.filmfolio.util.Resource<Unit>

    // DTO-based standard Firestore CRUD operations
    suspend fun addMovie(createMovieDto: CreateMovieDto): MovieDto
    suspend fun getMovies(): List<MovieDto>
    suspend fun updateMovie(movieId: String, updateDto: UpdateMovieDto)
    suspend fun deleteMovie(movieId: String)
}
