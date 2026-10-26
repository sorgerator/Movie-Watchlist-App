package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.dao.MovieDao
import com.example.filmfolio.data.local.dao.WatchlistDao
import com.example.filmfolio.data.local.entity.WatchlistItemEntity
import com.example.filmfolio.data.mappers.toDomain
import com.example.filmfolio.data.local.entity.WatchlistItem
import com.example.filmfolio.data.repository.WatchlistRepository
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
                        movieDao.getMovieByIdFlow(item.movieId),
                        movieDao.getGenresForMovie(item.movieId)
                    ) { movieEntity, genres ->
                        movieEntity?.let {
                            item.toDomain(it.toDomain(genres.map { it.toDomain() }))
                        }
                    }
                }
                combine(flowList) { list -> list.filterNotNull() }
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
