package com.example.movie_watchlist_app.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val loggedInUserId: Flow<Long?>
    suspend fun saveSession(userId: Long)
    suspend fun clearSession()
}
