package com.example.filmfolio.data.local.db

import com.example.filmfolio.data.local.dao.MovieDao
import com.example.filmfolio.data.local.entity.GenreEntity
import com.example.filmfolio.data.local.entity.MovieEntity
import com.example.filmfolio.data.local.entity.MovieGenreCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val movieDao: MovieDao
) {
    suspend fun seed() {
        if (movieDao.getMovieCount() > 0) return

        // Seed Genres
        val genres = listOf(
            GenreEntity(name = "Action"),
            GenreEntity(name = "Sci-Fi"),
            GenreEntity(name = "Drama"),
            GenreEntity(name = "Crime")
        )
        val genreIds = genres.map { movieDao.insertGenre(it) }
        val actionId = genreIds[0]
        val sciFiId = genreIds[1]
        val dramaId = genreIds[2]
        val crimeId = genreIds[3]

        // Seed Movies
        val inception = MovieEntity(
            title = "Inception",
            genre = "Sci-Fi, Action",
            description = "A thief who steals corporate secrets through the use of dream-sharing technology.",
            releaseDate = "2010"
        )
        val darkKnight = MovieEntity(
            title = "The Dark Knight",
            genre = "Action, Crime, Drama",
            description = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham.",
            releaseDate = "2008",
            isFavorite = false
        )
        val interstellar = MovieEntity(
            title = "Interstellar",
            genre = "Sci-Fi, Drama",
            description = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
            releaseDate = "2014"
        )
        val pulpFiction = MovieEntity(
            title = "Pulp Fiction",
            genre = "Crime, Drama",
            description = "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits.",
            releaseDate = "1994",
            isFavorite = false
        )

        val inceptionId = movieDao.insertMovie(inception)
        val darkKnightId = movieDao.insertMovie(darkKnight)
        val interstellarId = movieDao.insertMovie(interstellar)
        val pulpFictionId = movieDao.insertMovie(pulpFiction)

        // Seed CrossRefs
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(inceptionId, sciFiId))
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(inceptionId, actionId))
        
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(darkKnightId, actionId))
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(darkKnightId, crimeId))
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(darkKnightId, dramaId))
        
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(interstellarId, sciFiId))
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(interstellarId, dramaId))
        
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(pulpFictionId, crimeId))
        movieDao.insertMovieGenreCrossRef(MovieGenreCrossRef(pulpFictionId, dramaId))
    }
}
