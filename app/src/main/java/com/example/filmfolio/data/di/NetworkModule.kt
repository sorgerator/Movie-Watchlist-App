package com.example.filmfolio.data.di

import com.google.firebase.auth.FirebaseAuth
import com.example.filmfolio.data.remote.AuthApiService
import com.example.filmfolio.data.remote.MovieApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * A thread-safe global reference session manager for reference token storage.
 * In production, you would read this from your Encrypted SharedPreferences or Android Jetpack DataStore.
 */
object SessionManager {
    private var token: String? = null
    var email: String? = null
    var fullName: String? = null

    @Synchronized
    fun getActiveToken(): String? = token

    @Synchronized
    fun saveSession(newToken: String) {
        token = newToken
    }

    @Synchronized
    fun clearSession() {
        token = null
        email = null
        fullName = null
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        firebaseAuth: FirebaseAuth
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                
                val token = if (com.example.filmfolio.util.AppConfig.useFirebase) {
                    firebaseAuth.currentUser?.uid
                } else {
                    SessionManager.getActiveToken()
                }
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(
        retrofit: Retrofit
    ): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(
        retrofit: Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}
