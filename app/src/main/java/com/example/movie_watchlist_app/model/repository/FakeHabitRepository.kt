package com.example.movie_watchlist_app.model.repository

import com.example.movie_watchlist_app.model.HabitModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeHabitRepository {
    suspend fun getHabits(shouldFail: Boolean = false): List<HabitModel> = withContext(Dispatchers.IO) {
        delay(1500)
        if (shouldFail) {
            throw IllegalStateException("Failed to load habits.")
        }
        listOf(
            HabitModel(1, "Watch 1 movie per week"),
            HabitModel(2, "Update watchlist daily")
        )
    }
}
