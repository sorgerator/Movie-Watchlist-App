package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import com.example.movie_watchlist_app.presentation.ui.components.*
import com.example.movie_watchlist_app.presentation.util.AuthValidators

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hasTriedSubmit by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }

    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null

    val isLoginEnabled = email.isNotBlank() && password.isNotBlank()

    LoginScreen(
        email = email,
        password = password,
        emailError = emailError,
        passwordError = passwordError,
        passwordVisible = passwordVisible,
        isLoginEnabled = isLoginEnabled,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        onLoginClick = {
            hasTriedSubmit = true
            if (AuthValidators.validateEmail(email) == null && AuthValidators.validatePassword(password) == null) {
                onLoginSuccess()
            }
        },
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
private fun LoginScreen(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    passwordVisible: Boolean,
    isLoginEnabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(
                title = "Welcome Back",
                subtitle = "Login to access your movies"
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AuthTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    error = emailError
                )
                PasswordTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    error = passwordError,
                    isVisible = passwordVisible,
                    onVisibilityToggle = onPasswordVisibilityToggle
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                AuthPrimaryButton(
                    text = "Login",
                    enabled = isLoginEnabled,
                    onClick = onLoginClick)
                AuthDivider()
                SocialSignInButton(
                    text = "Continue with Google",
                    onClick = {}
                )
            }

            AuthFooterText(
                normalText = "Don't have an account?",
                actionText = "Register",
                onActionClick = onNavigateToRegister
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MovieWatchlistTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = {}
            )
        }
    }
}