package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.entity.Genre
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.data.repository.MovieRepository
import com.example.filmfolio.data.remote.CreateMovieDto
import com.example.filmfolio.data.remote.MovieDto
import com.example.filmfolio.data.remote.UpdateMovieDto
import com.example.filmfolio.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMovieRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MovieRepository {

    // Standard DTO-based Firestore CRUD operations

    override suspend fun addMovie(createMovieDto: CreateMovieDto): MovieDto {
        return try {
            val docData = hashMapOf(
                "title" to createMovieDto.title,
                "genre" to createMovieDto.genre,
                "description" to createMovieDto.description,
                "priority" to createMovieDto.priority,
                "isWatched" to createMovieDto.isWatched,
                "isFavorite" to createMovieDto.isFavorite,
                "releaseDate" to createMovieDto.releaseDate,
                "posterUrl" to createMovieDto.posterUrl,
                "userId" to (auth.currentUser?.uid ?: "")
            )
            val docRef = firestore.collection("movies").add(docData).await()
            MovieDto(
                id = docRef.id.hashCode().toLong(),
                title = createMovieDto.title,
                genre = createMovieDto.genre,
                description = createMovieDto.description,
                priority = createMovieDto.priority,
                isWatched = createMovieDto.isWatched,
                isFavorite = createMovieDto.isFavorite,
                releaseDate = createMovieDto.releaseDate,
                posterUrl = createMovieDto.posterUrl,
                userId = auth.currentUser?.uid ?: ""
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e
        }
    }

    override suspend fun getMovies(): List<MovieDto> {
        return try {
            val snapshot = firestore.collection("movies")
                .whereEqualTo("userId", auth.currentUser?.uid)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                val id = doc.id.hashCode().toLong()
                val title = doc.getString("title") ?: ""
                val genre = doc.getString("genre") ?: ""
                val description = doc.getString("description") ?: ""
                val priority = doc.getString("priority") ?: "Medium"
                val isWatched = doc.getBoolean("isWatched") ?: false
                val isFavorite = doc.getBoolean("isFavorite") ?: false
                val releaseDate = doc.getString("releaseDate") ?: ""
                val posterUrl = doc.getString("posterUrl") ?: ""
                val userId = doc.getString("userId") ?: ""
                MovieDto(
                    id = id,
                    title = title,
                    genre = genre,
                    description = description,
                    priority = priority,
                    isWatched = isWatched,
                    isFavorite = isFavorite,
                    releaseDate = releaseDate,
                    posterUrl = posterUrl,
                    userId = userId
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e
        }
    }

    override suspend fun updateMovie(movieId: String, updateDto: UpdateMovieDto) {
        try {
            val updateMap = mutableMapOf<String, Any>()
            updateDto.title?.let { updateMap["title"] = it }
            updateDto.genre?.let { updateMap["genre"] = it }
            updateDto.description?.let { updateMap["description"] = it }
            updateDto.priority?.let { updateMap["priority"] = it }
            updateDto.isWatched?.let { updateMap["isWatched"] = it }
            updateDto.isFavorite?.let { updateMap["isFavorite"] = it }
            updateDto.releaseDate?.let { updateMap["releaseDate"] = it }
            updateDto.posterUrl?.let { updateMap["posterUrl"] = it }
            updateDto.userId?.let { updateMap["userId"] = it }

            firestore.collection("movies")
                .document(movieId)
                .set(updateMap, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e
        }
    }

    override suspend fun deleteMovie(movieId: String) {
        try {
            firestore.collection("movies")
                .document(movieId)
                .delete()
                .await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e
        }
    }

    // Stub implementations for the rest of MovieRepository interface

    override suspend fun seedDatabase() {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun insertMovie(movie: Movie): Long {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun insertGenre(genre: Genre): Long {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun addGenreToMovie(movieId: Long, genreId: Long) {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override fun getAllMovies(): Flow<List<Movie>> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override fun getAllGenres(): Flow<List<Genre>> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override fun getGenresForMovie(movieId: Long): Flow<List<Genre>> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun getMovieById(movieId: Long): Movie? {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override fun getMovieByIdFlow(movieId: Long): Flow<Movie?> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun updateWatchedStatus(movieId: Long, isWatched: Boolean) {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun updateFavoriteStatus(movieId: Long, isFavorite: Boolean) {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun deleteMovie(movieId: Long) {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override fun getMoviesFromFirestore(): Flow<Resource<List<Movie>>> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun addMovieToFirestore(movie: Movie): Resource<Unit> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun updateWatchedStatusInFirestore(remoteId: String, isWatched: Boolean): Resource<Unit> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun updateFavoriteStatusInFirestore(remoteId: String, isFavorite: Boolean): Resource<Unit> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun deleteMovieFromFirestore(remoteId: String): Resource<Unit> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun getNetworkWatchlist(): Resource<List<Movie>> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun createNetworkMovie(movie: Movie): Resource<Movie> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun updateNetworkMovie(id: String, movie: Movie): Resource<Movie> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }

    override suspend fun deleteNetworkMovie(id: String): Resource<Unit> {
        throw UnsupportedOperationException("Not supported in Firebase repository")
    }
}
