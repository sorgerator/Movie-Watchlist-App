package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.entity.User
import com.example.filmfolio.util.Resource

data class RestAuthResult(
    val token: String,
    val user: User
)

interface AuthNetworkRepository {
    suspend fun login(email: String, pass: String): Resource<RestAuthResult>
    suspend fun register(email: String, pass: String, fullName: String): Resource<User>
}
