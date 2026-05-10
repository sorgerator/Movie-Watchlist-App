package com.example.movie_watchlist_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.model.repository.SessionRepository
import com.example.movie_watchlist_app.model.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Init : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onLoginClick(email: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                sessionRepository.saveUserId(user.id)
                _uiState.value = LoginUiState.Success
            } else {
                _uiState.value = LoginUiState.Error("User not found")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Init
    }
}
