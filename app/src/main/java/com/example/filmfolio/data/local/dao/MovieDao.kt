package com.example.filmfolio.data.local.dao

import androidx.room.*
import com.example.filmfolio.data.local.entity.GenreEntity
import com.example.filmfolio.data.local.entity.MovieEntity
import com.example.filmfolio.data.local.entity.MovieGenreCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: GenreEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRef(crossRef: MovieGenreCrossRef)

    @Transaction
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Long): MovieEntity?

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieByIdFlow(movieId: Long): Flow<MovieEntity?>

    @Transaction
    @Query("""
        SELECT * FROM genres 
        INNER JOIN movie_genre_cross_ref ON genres.id = movie_genre_cross_ref.genreId
        WHERE movie_genre_cross_ref.movieId = :movieId
    """)
    fun getGenresForMovie(movieId: Long): Flow<List<GenreEntity>>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int

    @Query("SELECT * FROM genres")
    fun getAllGenres(): Flow<List<GenreEntity>>

    @Query("UPDATE movies SET isWatched = :isWatched WHERE id = :movieId")
    suspend fun updateWatchedStatus(movieId: Long, isWatched: Boolean)

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: Long, isFavorite: Boolean)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Long)

    @Transaction
    @Query("SELECT * FROM movies")
    suspend fun getAllMoviesDirect(): List<MovieEntity>
}
