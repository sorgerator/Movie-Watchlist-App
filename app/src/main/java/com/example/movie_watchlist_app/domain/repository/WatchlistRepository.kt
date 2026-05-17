package com.example.movie_watchlist_app.domain.repository

import com.example.movie_watchlist_app.domain.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun addToWatchlist(userId: Long, movieId: Long, status: String): Long
    fun getWatchlistForUser(userId: Long): Flow<List<WatchlistItem>>
    suspend fun updateStatus(userId: Long, movieId: Long, status: String)
    suspend fun removeFromWatchlist(userId: Long, movieId: Long)
}
