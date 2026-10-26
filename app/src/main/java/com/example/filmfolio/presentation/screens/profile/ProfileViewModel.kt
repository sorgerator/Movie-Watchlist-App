package com.example.filmfolio.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfolio.data.repository.AuthRepository
import com.example.filmfolio.data.repository.SessionRepository
import com.example.filmfolio.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationEvents = Channel<ProfileNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProfileUiState> = sessionRepository.loggedInUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    flowOf(ProfileUiState.Success(user))
                } else {
                    flowOf(ProfileUiState.Error("User not found"))
                }
            } else {
                flowOf(ProfileUiState.Error("Not logged in"))
            }
        }
        .onStart { emit(ProfileUiState.Loading) }
        .catch { e -> 
            if (e is CancellationException) throw e
            emit(ProfileUiState.Error(e.message ?: "An error occurred")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState.Init
        )

    fun onLogoutClick() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                sessionRepository.clearSession()
                _navigationEvents.send(ProfileNavigationEvent.Logout)
            } catch (e: CancellationException) {
                throw e
            }
        }
    }
}
