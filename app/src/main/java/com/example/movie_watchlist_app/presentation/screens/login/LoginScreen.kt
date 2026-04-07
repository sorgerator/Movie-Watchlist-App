package com.example.movie_watchlist_app.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.components.*
import com.example.movie_watchlist_app.presentation.util.AuthValidators
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasTriedSubmit by remember { mutableStateOf(false) }

    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AuthHeader("Welcome Back", "Login to access your movies")
            Spacer(Modifier.height(32.dp))
            AuthTextField(email, { email = it }, "Email", emailError)
            Spacer(Modifier.height(16.dp))
            PasswordTextField(
                password,
                { password = it },
                "Password",
                passwordError,
                isVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible }
            )
            Spacer(Modifier.height(32.dp))
            AuthPrimaryButton("Login", onClick = {
                hasTriedSubmit = true
                if (emailError == null && passwordError == null) onLoginSuccess()
            })
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