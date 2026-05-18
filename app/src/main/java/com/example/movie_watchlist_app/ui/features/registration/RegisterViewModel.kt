package com.example.movie_watchlist_app.ui.features.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<RegisterNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onRegisterClick(fullName: String, email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val result = userRepository.register(fullName, email, pass)
                if (result > 0) {
                    _uiState.value = RegisterUiState.Success
                    _navigationEvents.send(RegisterNavigationEvent.NavigateBackToLogin)
                } else {
                    _uiState.value = RegisterUiState.Error("Registration failed")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _navigationEvents.send(RegisterNavigationEvent.NavigateBackToLogin)
        }
    }
}
