package com.example.filmfolio.data.local.dao

import androidx.room.*
import com.example.filmfolio.data.local.entity.WatchlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistItemEntity): Long

    @Query("SELECT * FROM watchlist_items WHERE userId = :userId")
    fun getWatchlistForUser(userId: Long): Flow<List<WatchlistItemEntity>>

    @Query("SELECT * FROM watchlist_items WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    suspend fun getWatchlistItem(userId: Long, movieId: Long): WatchlistItemEntity?

    @Update
    suspend fun updateWatchlistItem(item: WatchlistItemEntity)

    @Delete
    suspend fun removeFromWatchlist(item: WatchlistItemEntity)
}
