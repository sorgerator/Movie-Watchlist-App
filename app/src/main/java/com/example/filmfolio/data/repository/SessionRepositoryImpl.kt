package com.example.filmfolio.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.filmfolio.data.repository.SessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

@Singleton
class SessionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionRepository {

    private object PreferencesKeys {
        val USER_ID = longPreferencesKey("user_id")
    }

    override val loggedInUserId: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            val userId = preferences[PreferencesKeys.USER_ID]
            if (userId == -1L) null else userId
        }

    override suspend fun saveSession(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    override suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = -1L
        }
    }
}
