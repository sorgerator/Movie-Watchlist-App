package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.model.repository.FakeProfileRepository
import com.example.movie_watchlist_app.model.repository.ProfileData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Init : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val profile: ProfileData, val stats: String, val rows: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(private val repository: FakeProfileRepository = FakeProfileRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val profileDeferred = async { repository.getProfile() }
                val statsDeferred = async { repository.getProfileStats() }
                val rowsDeferred = async { repository.getAdditionalRows() }

                val profile = profileDeferred.await()
                val stats = statsDeferred.await()
                val rows = rowsDeferred.await()

                _uiState.value = ProfileUiState.Success(profile, stats, rows)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalStateException) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("An unexpected error occurred")
            }
        }
    }

    fun resetUiState() {
        loadProfile()
    }
}
