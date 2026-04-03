package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.movie_watchlist_app.presentation.ui.components.*
import com.example.movie_watchlist_app.presentation.util.AuthValidators

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
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
    email: String, password: String, emailError: String?, passwordError: String?,
    passwordVisible: Boolean, isLoginEnabled: Boolean,
    onEmailChange: (String) -> Unit, onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit, onLoginClick: () -> Unit, onNavigateToRegister: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
            AuthHeader("Welcome Back", "Login to access your movies")
            Spacer(Modifier.height(32.dp))
            AuthTextField(email, onEmailChange, "Email", emailError)
            Spacer(Modifier.height(16.dp))
            PasswordTextField(
                value = password, onValueChange = onPasswordChange, label = "Password", error = passwordError,
                isVisible = passwordVisible, onVisibilityToggle = onPasswordVisibilityToggle
            )
            Spacer(Modifier.height(32.dp))
            AuthPrimaryButton("Login", enabled = isLoginEnabled, onClick = onLoginClick)
            Spacer(Modifier.height(24.dp))
            AuthDivider()
            Spacer(Modifier.height(24.dp))
            SocialSignInButton("Continue with Google", {})
            Spacer(Modifier.height(32.dp))
            AuthFooterText("Don't have an account?", "Register", onNavigateToRegister)
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