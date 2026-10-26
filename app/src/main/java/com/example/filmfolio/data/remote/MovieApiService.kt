package com.example.filmfolio.data.remote

import com.example.filmfolio.data.remote.CreateMovieDto
import com.example.filmfolio.data.remote.MovieDto
import com.example.filmfolio.data.remote.UpdateMovieDto
import retrofit2.Response
import retrofit2.http.*

interface MovieApiService {

    @GET("watchlist")
    suspend fun getWatchlist(): List<MovieDto>

    @POST("watchlist")
    suspend fun createMovie(
        @Body movie: CreateMovieDto
    ): MovieDto

    @PUT("watchlist/{id}")
    suspend fun updateMovie(
        @Path("id") id: String,
        @Body movie: UpdateMovieDto
    ): MovieDto

    @DELETE("watchlist/{id}")
    suspend fun deleteMovie(
        @Path("id") id: String
    ): Response<Unit>
}
