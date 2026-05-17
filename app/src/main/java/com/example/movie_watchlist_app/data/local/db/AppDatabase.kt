package com.example.movie_watchlist_app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movie_watchlist_app.data.local.dao.MovieDao
import com.example.movie_watchlist_app.data.local.dao.UserDao
import com.example.movie_watchlist_app.data.local.dao.WatchlistDao
import com.example.movie_watchlist_app.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        MovieEntity::class,
        GenreEntity::class,
        MovieGenreCrossRef::class,
        WatchlistItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun watchlistDao(): WatchlistDao
}
