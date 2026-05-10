package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.model.repository.SessionRepository
import com.example.movie_watchlist_app.model.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    object Init : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val name: String, val email: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            val userId = sessionRepository.loggedInUserId.firstOrNull()
            if (userId != null) {
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    _uiState.value = ProfileUiState.Success(user.fullName, user.email)
                } else {
                    _uiState.value = ProfileUiState.Error("User not found")
                }
            } else {
                _uiState.value = ProfileUiState.Error("Not logged in")
            }
        }
    }
}
