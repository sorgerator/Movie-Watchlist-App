package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun register(fullName: String, email: String, pass: String): Long
    suspend fun login(email: String, pass: String): User?
    suspend fun getUserById(id: Long): User?
    suspend fun getUserByEmail(email: String): User?
    fun getAllUsers(): Flow<List<User>>
}
