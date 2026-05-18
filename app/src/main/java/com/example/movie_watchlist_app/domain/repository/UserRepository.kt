package com.example.movie_watchlist_app.domain.repository

import com.example.movie_watchlist_app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun register(fullName: String, email: String, pass: String): Long
    suspend fun login(email: String, pass: String): User?
    suspend fun getUserById(id: Long): User?
    fun getAllUsers(): Flow<List<User>>
}
