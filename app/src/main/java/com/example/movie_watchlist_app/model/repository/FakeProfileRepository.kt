package com.example.movie_watchlist_app.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

data class ProfileData(val name: String, val email: String)

class FakeProfileRepository {
    suspend fun getProfile(shouldFail: Boolean = false): ProfileData = withContext(Dispatchers.IO) {
        delay(2000)
        if (shouldFail) {
            throw IllegalStateException("Failed to load profile.")
        }
        ProfileData("John Doe", "john.doe@example.com")
    }

    suspend fun getProfileStats(): String = withContext(Dispatchers.IO) {
        delay(1000)
        "Stats: 10 Movies Watched"
    }

    suspend fun getAdditionalRows(): String = withContext(Dispatchers.IO) {
        delay(1000)
        "Additional Rows: 5"
    }
}
