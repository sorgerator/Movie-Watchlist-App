package com.example.filmfolio.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.repository.AuthRepository
import com.example.filmfolio.data.repository.UserRepository
import com.example.filmfolio.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Init)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<RegisterNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onRegisterClick(fullName: String, email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val cleanName = fullName.trim()
                val cleanEmail = email.trim()
                when (val result = authRepository.register(cleanEmail, pass, cleanName)) {
                    is Resource.Success -> {
                        // Sync with local Room DB if not already inserted
                        if (userRepository.getUserByEmail(cleanEmail) == null) {
                            userRepository.register(cleanName, cleanEmail, pass)
                        }
                        _uiState.value = RegisterUiState.Success
                        _navigationEvents.send(RegisterNavigationEvent.NavigateBackToLogin)
                    }
                    is Resource.Error -> {
                        _uiState.value = RegisterUiState.Error(result.message)
                    }
                    is Resource.Loading -> {
                        _uiState.value = RegisterUiState.Loading
                    }
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
            try {
                _navigationEvents.send(RegisterNavigationEvent.NavigateBackToLogin)
            } catch (e: CancellationException) {
                throw e
            }
        }
    }
}
