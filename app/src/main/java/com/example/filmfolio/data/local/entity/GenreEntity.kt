package com.example.filmfolio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
