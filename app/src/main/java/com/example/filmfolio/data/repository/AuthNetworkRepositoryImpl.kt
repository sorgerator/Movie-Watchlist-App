package com.example.filmfolio.data.repository

import com.example.filmfolio.data.local.entity.User
import com.example.filmfolio.data.repository.AuthNetworkRepository
import com.example.filmfolio.data.repository.RestAuthResult
import com.example.filmfolio.data.remote.UserLoginDto
import com.example.filmfolio.data.remote.UserRegisterDto
import com.example.filmfolio.data.remote.UserResponseDto
import com.example.filmfolio.data.remote.AuthApiService
import com.example.filmfolio.util.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Extension to map the user network response DTO to domain model.
 */
fun UserResponseDto.toDomain(): User {
    return User(
        id = id,
        fullName = fullName,
        email = email,
        createdAt = createdAt
    )
}

@Singleton
class AuthNetworkRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthNetworkRepository {

    override suspend fun login(email: String, pass: String): Resource<RestAuthResult> = withContext(Dispatchers.IO) {
        try {
            val response = authApiService.login(UserLoginDto(email, pass))
            val user = response.user.toDomain()
            Resource.Success(RestAuthResult(response.token, user))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to log in via REST API")
        }
    }

    override suspend fun register(email: String, pass: String, fullName: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val response = authApiService.register(UserRegisterDto(email, pass, fullName))
            Resource.Success(response.toDomain())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to register via REST API")
        }
    }
}
