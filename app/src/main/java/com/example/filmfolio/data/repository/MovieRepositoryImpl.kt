package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.dao.MovieDao
import com.example.filmfolio.data.local.db.DatabaseInitializer
import com.example.filmfolio.data.local.entity.MovieGenreCrossRef
import com.example.filmfolio.data.mappers.toDomain
import com.example.filmfolio.data.mappers.toEntity
import com.example.filmfolio.data.local.entity.Genre
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.repository.MovieRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.filmfolio.util.Resource
import com.example.filmfolio.util.AppConfig
import com.example.filmfolio.data.remote.MovieApiService
import com.example.filmfolio.data.mappers.toDomain
import com.example.filmfolio.data.mappers.toCreateDto
import com.example.filmfolio.data.mappers.toUpdateDto
import com.example.filmfolio.data.remote.CreateMovieDto
import com.example.filmfolio.data.remote.MovieDto
import com.example.filmfolio.data.remote.UpdateMovieDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val watchlistDao: com.example.filmfolio.data.local.dao.WatchlistDao,
    private val databaseInitializer: DatabaseInitializer,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val apiService: MovieApiService,
    private val sessionRepository: SessionRepository
) : MovieRepository {

    private val userId: String?
        get() = auth.currentUser?.uid

    private val moviesCollection
        get() = userId?.let { firestore.collection("users").document(it).collection("movies") }

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

    override suspend fun updateWatchedStatus(movieId: Long, isWatched: Boolean) = withContext(Dispatchers.IO) {
        movieDao.updateWatchedStatus(movieId, isWatched)
    }

    override suspend fun updateFavoriteStatus(movieId: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }

    override suspend fun deleteMovie(movieId: Long) = withContext(Dispatchers.IO) {
        movieDao.deleteMovieById(movieId)
    }

    // Dynamic routing methods: Firestore / REST API / local Room fallback
    // Dynamic routing methods: Firestore / REST API / local Room fallback
    override fun getMoviesFromFirestore(): Flow<Resource<List<Movie>>> = callbackFlow {
        // Emit cached movies immediately to ensure high responsiveness and prevent navigation freezes
        launch(Dispatchers.IO) {
            try {
                val cached = movieDao.getAllMoviesDirect().map { entity ->
                    val genres = movieDao.getGenresForMovie(entity.id).first()
                    entity.toDomain(genres.map { it.toDomain() })
                }
                trySend(Resource.Success(cached))
            } catch (e: Exception) {
                trySend(Resource.Loading())
            }
        }

        if (AppConfig.useFirebase) {
            val collection = moviesCollection
            if (collection == null) {
                launch(Dispatchers.IO) {
                    try {
                        val movies = movieDao.getAllMoviesDirect().map { entity ->
                            val genres = movieDao.getGenresForMovie(entity.id).first()
                            entity.toDomain(genres.map { it.toDomain() })
                        }
                        trySend(Resource.Success(movies))
                    } catch (e: Exception) {
                        trySend(Resource.Error("User not authenticated & local fallback failed"))
                    }
                }
                awaitClose {}
                return@callbackFlow
            }

            val subscription = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    launch(Dispatchers.IO) {
                        try {
                            val movies = movieDao.getAllMoviesDirect().map { entity ->
                                val genres = movieDao.getGenresForMovie(entity.id).first()
                                entity.toDomain(genres.map { it.toDomain() })
                            }
                            trySend(Resource.Success(movies))
                        } catch (e: Exception) {
                            trySend(Resource.Error(error.message ?: "Firebase error & local fallback failed"))
                        }
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val movies = snapshot.documents.mapNotNull { doc ->
                        val movie = doc.toObject(MovieData::class.java)
                        movie?.toDomain(doc.id)
                    }

                    launch(Dispatchers.IO) {
                        // Synchronize Room database by deleting local movies that are no longer in the Firebase watchlist
                        val remoteIds = movies.map { it.id }.toSet()
                        val currentRoomMovies = movieDao.getAllMoviesDirect()
                        currentRoomMovies.forEach { localMovie ->
                            if (localMovie.id != 0L && localMovie.id !in remoteIds) {
                                movieDao.deleteMovieById(localMovie.id)
                            }
                        }

                        movies.forEach { movie ->
                            val localMovieId = if (movie.id == 0L) {
                                val newId = movieDao.insertMovie(movie.toEntity())
                                movie.remoteId?.let { remoteId ->
                                    collection.document(remoteId).update("id", newId)
                                }
                                newId
                            } else {
                                movieDao.insertMovie(movie.toEntity())
                                movie.id
                            }

                            // Sync local watchlist items
                            sessionRepository.loggedInUserId.first()?.let { userId ->
                                if (watchlistDao.getWatchlistItem(userId, localMovieId) == null) {
                                    watchlistDao.addToWatchlist(
                                        com.example.filmfolio.data.local.entity.WatchlistItemEntity(
                                            userId = userId,
                                            movieId = localMovieId,
                                            status = "Plan to Watch"
                                        )
                                    )
                                }
                            }
                        }
                    }
                    trySend(Resource.Success(movies))
                }
            }
            awaitClose { subscription.remove() }
        } else {
            launch(Dispatchers.IO) {
                try {
                    val response = apiService.getWatchlist()
                    val movies = response.map { it.toDomain() }

                    // Sync Room database: delete movies no longer on the custom REST API watchlist
                    val remoteIds = movies.map { it.id }.toSet()
                    val currentRoomMovies = movieDao.getAllMoviesDirect()
                    currentRoomMovies.forEach { localMovie ->
                        if (localMovie.id != 0L && localMovie.id !in remoteIds) {
                            movieDao.deleteMovieById(localMovie.id)
                        }
                    }

                    // Upsert newly fetched movies & sync local watchlist items
                    movies.forEach { movie ->
                        val localMovieId = movieDao.insertMovie(movie.toEntity())
                        sessionRepository.loggedInUserId.first()?.let { userId ->
                            if (watchlistDao.getWatchlistItem(userId, localMovieId) == null) {
                                watchlistDao.addToWatchlist(
                                    com.example.filmfolio.data.local.entity.WatchlistItemEntity(
                                        userId = userId,
                                        movieId = localMovieId,
                                        status = "Plan to Watch"
                                    )
                                )
                            }
                        }
                    }
                    trySend(Resource.Success(movies))
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    // Connection failed! Fallback to Room DB cache
                    try {
                        val movies = movieDao.getAllMoviesDirect().map { entity ->
                            val genres = movieDao.getGenresForMovie(entity.id).first()
                            entity.toDomain(genres.map { it.toDomain() })
                        }
                        trySend(Resource.Success(movies))
                    } catch (ex: Exception) {
                        trySend(Resource.Error(e.message ?: "REST API error & local fallback failed"))
                    }
                }
            }
            awaitClose {}
        }
    }

    override suspend fun addMovieToFirestore(movie: Movie): Resource<Unit> {
        return try {
            if (AppConfig.useFirebase) {
                val collection = moviesCollection ?: return Resource.Error("User not authenticated")
                val movieData = MovieData.fromDomain(movie)
                collection.add(movieData).await()
                Resource.Success(Unit)
            } else {
                val response = apiService.createMovie(movie.toCreateDto())
                
                // Delete the temporary movie record that was created locally with auto-generated ID
                movieDao.deleteMovieById(movie.id)
                
                // Insert the new movie record with correct backend ID
                val updatedMovie = movie.copy(id = response.id, remoteId = response.id.toString())
                movieDao.insertMovie(updatedMovie.toEntity())
                
                // Re-insert matching local watchlist item with the correct backend ID for the user
                sessionRepository.loggedInUserId.first()?.let { userId ->
                    watchlistDao.addToWatchlist(
                        com.example.filmfolio.data.local.entity.WatchlistItemEntity(
                            userId = userId,
                            movieId = response.id,
                            status = "Plan to Watch"
                        )
                    )
                }
                
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            // Offline fallback: Room is already updated locally by the ViewModel, so return success.
            Resource.Success(Unit)
        }
    }

    override suspend fun updateWatchedStatusInFirestore(remoteId: String, isWatched: Boolean): Resource<Unit> {
        return try {
            if (AppConfig.useFirebase) {
                val collection = moviesCollection ?: return Resource.Error("User not authenticated")
                collection.document(remoteId).update("watched", isWatched).await()
                Resource.Success(Unit)
            } else {
                apiService.updateMovie(remoteId, UpdateMovieDto(isWatched = isWatched))
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            // Offline fallback: Room is already updated locally by the ViewModel, so return success.
            Resource.Success(Unit)
        }
    }

    override suspend fun updateFavoriteStatusInFirestore(remoteId: String, isFavorite: Boolean): Resource<Unit> {
        return try {
            if (AppConfig.useFirebase) {
                val collection = moviesCollection ?: return Resource.Error("User not authenticated")
                collection.document(remoteId).update("favorite", isFavorite).await()
                Resource.Success(Unit)
            } else {
                apiService.updateMovie(remoteId, UpdateMovieDto(isFavorite = isFavorite))
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            // Offline fallback
            Resource.Success(Unit)
        }
    }

    override suspend fun deleteMovieFromFirestore(remoteId: String): Resource<Unit> {
        return try {
            if (AppConfig.useFirebase) {
                val collection = moviesCollection ?: return Resource.Error("User not authenticated")
                collection.document(remoteId).delete().await()
                Resource.Success(Unit)
            } else {
                apiService.deleteMovie(remoteId)
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            // Offline fallback
            Resource.Success(Unit)
        }
    }

    // Retrofit network methods directly exposing responses (stubs or direct calls)
    override suspend fun getNetworkWatchlist(): Resource<List<Movie>> {
        return try {
            val response = apiService.getWatchlist()
            Resource.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "Error fetching network watchlist")
        }
    }

    override suspend fun createNetworkMovie(movie: Movie): Resource<Movie> {
        return try {
            val response = apiService.createMovie(movie.toCreateDto())
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "Error creating movie on network")
        }
    }

    override suspend fun updateNetworkMovie(id: String, movie: Movie): Resource<Movie> {
        return try {
            val response = apiService.updateMovie(id, movie.toUpdateDto())
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "Error updating movie on network")
        }
    }

    override suspend fun deleteNetworkMovie(id: String): Resource<Unit> {
        return try {
            val response = apiService.deleteMovie(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Failed to delete movie on network: ${response.code()}")
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Resource.Error(e.message ?: "Error deleting movie on network")
        }
    }

    override suspend fun addMovie(createMovieDto: CreateMovieDto): MovieDto {
        throw UnsupportedOperationException("Not implemented in local repository")
    }

    override suspend fun getMovies(): List<MovieDto> {
        throw UnsupportedOperationException("Not implemented in local repository")
    }

    override suspend fun updateMovie(movieId: String, updateDto: UpdateMovieDto) {
        throw UnsupportedOperationException("Not implemented in local repository")
    }

    override suspend fun deleteMovie(movieId: String) {
        throw UnsupportedOperationException("Not implemented in local repository")
    }
}

// Data class for Firestore serialization
data class MovieData(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val genre: String = "",
    val priority: String = "Medium",
    val watched: Boolean = false,
    val favorite: Boolean = false,
    val releaseDate: String = "",
    val posterUrl: String = ""
) {
    fun toDomain(remoteId: String): Movie {
        return Movie(
            id = id,
            remoteId = remoteId,
            title = title,
            description = description,
            genre = genre,
            priority = priority,
            isWatched = watched,
            isFavorite = favorite,
            releaseDate = releaseDate,
            posterUrl = posterUrl
        )
    }

    companion object {
        fun fromDomain(movie: Movie): MovieData {
            return MovieData(
                id = movie.id,
                title = movie.title,
                description = movie.description,
                genre = movie.genre,
                priority = movie.priority,
                watched = movie.isWatched,
                favorite = movie.isFavorite,
                releaseDate = movie.releaseDate,
                posterUrl = movie.posterUrl
            )
        }
    }
}
