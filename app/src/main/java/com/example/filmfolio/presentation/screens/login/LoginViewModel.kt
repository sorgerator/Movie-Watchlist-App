package com.example.filmfolio.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.repository.AuthRepository
import com.example.filmfolio.data.repository.SessionRepository
import com.example.filmfolio.data.repository.UserRepository
import com.example.filmfolio.util.Resource
import com.example.filmfolio.util.AppConfig
import com.example.filmfolio.data.di.SessionManager
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<LoginNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            try {
                val currentUser = if (AppConfig.useFirebase) authRepository.getCurrentUser() else null
                if (currentUser?.email != null) {
                    val emailVal = currentUser.email!!
                    val existingLocal = userRepository.getUserByEmail(emailVal)
                    val nameVal = currentUser.displayName?.takeIf { it.isNotBlank() }
                        ?: existingLocal?.fullName?.takeIf { it.isNotBlank() }
                        ?: "User"
                    syncLocalUserAndSaveSession(emailVal, nameVal)
                    _navigationEvents.send(LoginNavigationEvent.NavigateToHome)
                } else {
                    val savedUserId = sessionRepository.loggedInUserId.first()
                    if (savedUserId != null) {
                        val user = userRepository.getUserById(savedUserId)
                        if (user != null) {
                            SessionManager.email = user.email
                            SessionManager.fullName = user.fullName
                            SessionManager.saveSession("local_token_${user.id}")
                            _navigationEvents.send(LoginNavigationEvent.NavigateToHome)
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Ignore silent initialization errors
            }
        }
    }

    private suspend fun syncLocalUserAndSaveSession(email: String, displayName: String, password: String = ""): Long {
        val cleanEmail = email.trim()
        val existingUser = userRepository.getUserByEmail(cleanEmail)
        val userId = if (existingUser != null) {
            existingUser.id
        } else {
            userRepository.register(displayName, cleanEmail, password)
        }
        sessionRepository.saveSession(userId)
        return userId
    }

    fun onLoginClick(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val cleanEmail = email.trim()
                when (val result = authRepository.login(cleanEmail, pass)) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        val existingLocal = userRepository.getUserByEmail(cleanEmail)
                        val emailVal = user?.email ?: SessionManager.email ?: existingLocal?.email ?: cleanEmail
                        val displayNameVal = user?.displayName?.takeIf { it.isNotBlank() }
                            ?: SessionManager.fullName?.takeIf { it.isNotBlank() }
                            ?: existingLocal?.fullName?.takeIf { it.isNotBlank() }
                            ?: "User"
                        syncLocalUserAndSaveSession(emailVal, displayNameVal, pass)
                        _uiState.value = LoginUiState.Success(displayNameVal)
                        _navigationEvents.send(LoginNavigationEvent.NavigateToHome)
                    }
                    is Resource.Error -> {
                        _uiState.value = LoginUiState.Error(result.message)
                    }
                    is Resource.Loading -> {
                        _uiState.value = LoginUiState.Loading
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onGoogleSignInClick(context: Context) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                when (val result = authRepository.signInWithGoogle(context)) {
                    is Resource.Success -> {
                        val user = result.data?.user
                        val emailVal = user?.email
                        val displayNameVal = user?.displayName ?: "Google User"
                        if (emailVal != null) {
                            syncLocalUserAndSaveSession(emailVal, displayNameVal)
                            _uiState.value = LoginUiState.Success(displayNameVal)
                            _navigationEvents.send(LoginNavigationEvent.NavigateToHome)
                        } else {
                            _uiState.value = LoginUiState.Error("Google account does not provide an email address")
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = LoginUiState.Error(result.message)
                    }
                    is Resource.Loading -> {
                        _uiState.value = LoginUiState.Loading
                    }
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
            try {
                _navigationEvents.send(LoginNavigationEvent.NavigateToRegister)
            } catch (e: CancellationException) {
                throw e
            }
        }
    }
}
