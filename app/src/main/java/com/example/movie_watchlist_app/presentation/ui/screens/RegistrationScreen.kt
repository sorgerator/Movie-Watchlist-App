package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.ui.components.*
import com.example.movie_watchlist_app.presentation.util.AuthValidators
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

@Composable
fun RegistrationScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var hasTriedSubmit by remember { mutableStateOf(false) }

    val nameError = if (hasTriedSubmit) AuthValidators.validateFullName(fullName) else null
    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null
    val confirmError = if (hasTriedSubmit) AuthValidators.validateConfirmPassword(password, confirmPassword) else null

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            AuthHeader("Create Account", "Start building your watchlist")
            Spacer(Modifier.height(24.dp))
            AuthTextField(fullName, { fullName = it }, "Full Name", nameError)
            Spacer(Modifier.height(16.dp))
            AuthTextField(email, { email = it }, "Email", emailError)
            Spacer(Modifier.height(16.dp))
            PasswordTextField(password, { password = it }, "Password", passwordError)
            Spacer(Modifier.height(16.dp))
            PasswordTextField(confirmPassword, { confirmPassword = it }, "Confirm Password", confirmError)
            Spacer(Modifier.height(24.dp))
            AuthPrimaryButton("Register", onClick = {
                hasTriedSubmit = true
                if (nameError == null && emailError == null && passwordError == null && confirmError == null) {
                    onRegisterSuccess()
                }
            })
            Spacer(Modifier.height(16.dp))
            AuthDivider()
            Spacer(Modifier.height(16.dp))
            SocialSignInButton("Sign up with Google", {})
            Spacer(Modifier.height(24.dp))
            AuthFooterText("Already have an account?", "Login", onNavigateToLogin)
        }
    }
}

@Preview(showBackground = true, name = "Registration Screen Light Mode")
@Composable
fun RegistrationScreenPreview() {
    MovieWatchlistTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RegistrationScreen(
                onRegisterSuccess = {},
                onNavigateToLogin = {}
            )
        }
    }
}