package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.entity.WatchlistItem
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun addToWatchlist(userId: Long, movieId: Long, status: String): Long
    fun getWatchlistForUser(userId: Long): Flow<List<WatchlistItem>>
    suspend fun updateStatus(userId: Long, movieId: Long, status: String)
    suspend fun removeFromWatchlist(userId: Long, movieId: Long)
}
