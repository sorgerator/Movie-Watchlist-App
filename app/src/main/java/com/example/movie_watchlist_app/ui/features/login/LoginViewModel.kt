package com.example.movie_watchlist_app.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_watchlist_app.domain.repository.SessionRepository
import com.example.movie_watchlist_app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<LoginNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onLoginClick(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val user = userRepository.login(email, pass)
                if (user != null) {
                    sessionRepository.saveSession(user.id)
                    _uiState.value = LoginUiState.Success(user.fullName)
                    _navigationEvents.send(LoginNavigationEvent.NavigateToHome)
                } else {
                    _uiState.value = LoginUiState.Error("Invalid credentials")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            _navigationEvents.send(LoginNavigationEvent.NavigateToRegister)
        }
    }
}
