package com.example.filmfolio.data.remote

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("description") val description: String,
    @SerializedName("priority") val priority: String = "Medium",
    @SerializedName("is_watched") val isWatched: Boolean = false,
    @SerializedName("is_favorite") val isFavorite: Boolean = false,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("poster_url") val posterUrl: String = "",
    @SerializedName("user_id") val userId: String
)

data class CreateMovieDto(
    @SerializedName("title") val title: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("description") val description: String,
    @SerializedName("priority") val priority: String = "Medium",
    @SerializedName("is_watched") val isWatched: Boolean = false,
    @SerializedName("is_favorite") val isFavorite: Boolean = false,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("poster_url") val posterUrl: String = "",
    @SerializedName("user_id") val userId: String = ""
)

data class UpdateMovieDto(
    @SerializedName("title") val title: String? = null,
    @SerializedName("genre") val genre: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("priority") val priority: String? = null,
    @SerializedName("is_watched") val isWatched: Boolean? = null,
    @SerializedName("is_favorite") val isFavorite: Boolean? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("poster_url") val posterUrl: String? = null,
    @SerializedName("user_id") val userId: String? = null
)
