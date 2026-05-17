package com.example.movie_watchlist_app.data.repository

import com.example.movie_watchlist_app.data.local.dao.MovieDao
import com.example.movie_watchlist_app.data.local.dao.WatchlistDao
import com.example.movie_watchlist_app.data.local.entity.WatchlistItemEntity
import com.example.movie_watchlist_app.data.mapper.toDomain
import com.example.movie_watchlist_app.domain.model.WatchlistItem
import com.example.movie_watchlist_app.domain.repository.WatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(
    private val watchlistDao: WatchlistDao,
    private val movieDao: MovieDao
) : WatchlistRepository {

    override suspend fun addToWatchlist(userId: Long, movieId: Long, status: String): Long = withContext(Dispatchers.IO) {
        watchlistDao.addToWatchlist(WatchlistItemEntity(userId = userId, movieId = movieId, status = status))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWatchlistForUser(userId: Long): Flow<List<WatchlistItem>> = 
        watchlistDao.getWatchlistForUser(userId).flatMapLatest { items ->
            if (items.isEmpty()) flowOf(emptyList())
            else {
                val flowList = items.map { item ->
                    combine(
                        movieDao.getMovieByIdFlow(item.movieId).filterNotNull(),
                        movieDao.getGenresForMovie(item.movieId)
                    ) { movieEntity, genres ->
                        item.toDomain(movieEntity.toDomain(genres.map { it.toDomain() }))
                    }
                }
                combine(flowList) { it.toList() }
            }
        }

    override suspend fun updateStatus(userId: Long, movieId: Long, status: String) {
        withContext(Dispatchers.IO) {
            watchlistDao.getWatchlistItem(userId, movieId)?.let {
                watchlistDao.updateWatchlistItem(it.copy(status = status))
            }
        }
    }

    override suspend fun removeFromWatchlist(userId: Long, movieId: Long) {
        withContext(Dispatchers.IO) {
            watchlistDao.getWatchlistItem(userId, movieId)?.let {
                watchlistDao.removeFromWatchlist(it)
            }
        }
    }
}
