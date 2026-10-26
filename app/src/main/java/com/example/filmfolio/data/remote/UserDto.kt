package com.example.filmfolio.data.remote

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("createdAt") val createdAt: Long
)

data class UserRegisterDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("fullName") val fullName: String
)

data class UserLoginDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class TokenResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserResponseDto
)
