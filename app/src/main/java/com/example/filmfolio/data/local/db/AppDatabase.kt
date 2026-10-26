package com.example.filmfolio.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.filmfolio.data.local.dao.MovieDao
import com.example.filmfolio.data.local.dao.UserDao
import com.example.filmfolio.data.local.dao.WatchlistDao
import com.example.filmfolio.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        MovieEntity::class,
        GenreEntity::class,
        MovieGenreCrossRef::class,
        WatchlistItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun watchlistDao(): WatchlistDao
}
