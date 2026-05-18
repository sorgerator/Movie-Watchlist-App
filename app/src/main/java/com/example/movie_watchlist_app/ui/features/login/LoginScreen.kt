package com.example.movie_watchlist_app.ui.features.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.ui.components.*
import com.example.movie_watchlist_app.ui.util.AuthValidators

import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasTriedSubmit by remember { mutableStateOf(false) }

    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                LoginNavigationEvent.NavigateToHome -> onNavigateToHome()
                LoginNavigationEvent.NavigateToRegister -> onNavigateToRegister()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    AuthHeader(
                        stringResource(R.string.welcome_back),
                        stringResource(R.string.login_subtitle)
                    )
                    Spacer(Modifier.height(32.dp))
                    AuthTextField(
                        email,
                        { email = it },
                        stringResource(R.string.label_email),
                        emailError
                    )
                    Spacer(Modifier.height(16.dp))
                    PasswordTextField(
                        password,
                        { password = it },
                        stringResource(R.string.label_password),
                        passwordError,
                        isVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible }
                    )

                    if (uiState is LoginUiState.Error) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (uiState as LoginUiState.Error).message ?: stringResource(R.string.error_unknown),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    AuthPrimaryButton(
                        stringResource(R.string.btn_login),
                        onClick = {
                            hasTriedSubmit = true
                            if (emailError == null && passwordError == null) {
                                viewModel.onLoginClick(email, password)
                            }
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                    AuthDivider()
                    Spacer(Modifier.height(24.dp))
                    SocialSignInButton(
                        stringResource(R.string.btn_google_signin),
                        {}
                    )
                    Spacer(Modifier.height(32.dp))
                    AuthFooterText(
                        stringResource(R.string.no_account),
                        stringResource(R.string.register_now),
                        onNavigateToRegister
                    )
                }
            }
        }
    }
}
