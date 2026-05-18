package com.example.movie_watchlist_app.di

import android.content.Context
import androidx.room.Room
import com.example.movie_watchlist_app.data.local.dao.MovieDao
import com.example.movie_watchlist_app.data.local.dao.UserDao
import com.example.movie_watchlist_app.data.local.dao.WatchlistDao
import com.example.movie_watchlist_app.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "movie_app_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()

    @Provides
    fun provideWatchlistDao(db: AppDatabase): WatchlistDao = db.watchlistDao()
}
