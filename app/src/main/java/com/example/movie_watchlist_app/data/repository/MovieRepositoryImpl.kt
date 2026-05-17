package com.example.movie_watchlist_app.data.repository

import com.example.movie_watchlist_app.data.local.dao.MovieDao
import com.example.movie_watchlist_app.data.local.db.DatabaseInitializer
import com.example.movie_watchlist_app.data.local.entity.MovieGenreCrossRef
import com.example.movie_watchlist_app.data.mapper.toDomain
import com.example.movie_watchlist_app.data.mapper.toEntity
import com.example.movie_watchlist_app.domain.model.Genre
import com.example.movie_watchlist_app.domain.model.Movie
import com.example.movie_watchlist_app.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val databaseInitializer: DatabaseInitializer
) : MovieRepository {

    override suspend fun seedDatabase() {
        databaseInitializer.seed()
    }

    override suspend fun insertMovie(movie: Movie): Long = withContext(Dispatchers.IO) {
        movieDao.insertMovie(movie.toEntity())
    }

    override suspend fun insertGenre(genre: Genre): Long = withContext(Dispatchers.IO) {
        movieDao.insertGenre(genre.toEntity())
    }

    override suspend fun addGenreToMovie(movieId: Long, genreId: Long) = withContext(Dispatchers.IO) {
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(movieId, genreId))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllMovies(): Flow<List<Movie>> = movieDao.getAllMovies().flatMapLatest { movies ->
        if (movies.isEmpty()) flowOf(emptyList())
        else {
            val movieFlows = movies.map { entity ->
                movieDao.getGenresForMovie(entity.id).map { genres ->
                    entity.toDomain(genres.map { it.toDomain() })
                }
            }
            combine(movieFlows) { it.toList() }
        }
    }

    override fun getAllGenres(): Flow<List<Genre>> = movieDao.getAllGenres().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getGenresForMovie(movieId: Long): Flow<List<Genre>> = 
        movieDao.getGenresForMovie(movieId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getMovieById(movieId: Long): Movie? = withContext(Dispatchers.IO) {
        movieDao.getMovieById(movieId)?.toDomain()
    }

    override fun getMovieByIdFlow(movieId: Long): Flow<Movie?> = 
        movieDao.getMovieByIdFlow(movieId).map { it?.toDomain() }
}
