package com.example.filmfolio.data.remote

import com.example.filmfolio.data.remote.UserLoginDto
import com.example.filmfolio.data.remote.UserRegisterDto
import com.example.filmfolio.data.remote.UserResponseDto
import com.example.filmfolio.data.remote.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: UserRegisterDto
    ): UserResponseDto

    @POST("auth/login")
    suspend fun login(
        @Body request: UserLoginDto
    ): TokenResponseDto
}
