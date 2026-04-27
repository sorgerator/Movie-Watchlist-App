package com.example.movie_watchlist_app.presentation.ui.screens.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.example.movie_watchlist_app.presentation.ui.components.*
import com.example.movie_watchlist_app.presentation.util.AuthValidators

@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateClick: () -> Unit
) {
    var fullName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }
    var hasTriedSubmit by remember {
        mutableStateOf(false)
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }
    var confirmVisible by remember {
        mutableStateOf(false)
    }

    val nameError = if (hasTriedSubmit) AuthValidators.validateFullName(fullName) else null
    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null
    val confirmError = if (hasTriedSubmit) AuthValidators.validateConfirmPassword(password, confirmPassword) else null

    val isRegisterEnabled = fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    RegistrationScreen(
        fullName = fullName,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        nameError = nameError,
        emailError = emailError,
        passwordError = passwordError,
        confirmError = confirmError,
        passwordVisible = passwordVisible,
        confirmVisible = confirmVisible,
        isRegisterEnabled = isRegisterEnabled,
        onFullNameChange = {
            fullName = it
        },
        onEmailChange = {
            email = it
        },
        onPasswordChange = {
            password = it
        },
        onConfirmPasswordChange = {
            confirmPassword = it
        },
        onPasswordVisibilityToggle = {
            passwordVisible = !passwordVisible
        },
        onConfirmVisibilityToggle = {
            confirmVisible = !confirmVisible
        },
        onRegisterClick = {
            hasTriedSubmit = true
            if (AuthValidators.validateFullName(fullName) == null && AuthValidators.validateEmail(email) == null &&
                AuthValidators.validatePassword(password) == null && AuthValidators.validateConfirmPassword(password, confirmPassword) == null) {
                onRegisterSuccess()
            }
        },
        onNavigateClick = onNavigateClick
    )
}

@Composable
private fun RegistrationScreen(
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    nameError: String?,
    emailError: String?,
    passwordError: String?,
    confirmError: String?,
    passwordVisible: Boolean,
    confirmVisible: Boolean,
    isRegisterEnabled: Boolean,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onConfirmVisibilityToggle: () -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(
            rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            AuthHeader(
                "Create Account",
                "Start building your watchlist"
            )
            Spacer(Modifier.height(24.dp))
            AuthTextField(
                fullName,
                onFullNameChange,
                "Full Name",
                nameError,
                leadingIcon = Icons.Default.Person
            )
            Spacer(Modifier.height(16.dp))
            AuthTextField(
                email,
                onEmailChange,
                "Email",
                emailError
            )
            Spacer(Modifier.height(16.dp))
            PasswordTextField(
                password,
                onPasswordChange,
                "Password",
                passwordError,
                isVisible = passwordVisible,
                onVisibilityToggle = onPasswordVisibilityToggle
            )
            Spacer(Modifier.height(16.dp))
            PasswordTextField(
                confirmPassword,
                onConfirmPasswordChange,
                "Confirm Password",
                confirmError,
                isVisible = confirmVisible,
                onVisibilityToggle = onConfirmVisibilityToggle
            )
            Spacer(Modifier.height(24.dp))
            AuthPrimaryButton(
                "Register",
                enabled = isRegisterEnabled,
                onClick = onRegisterClick
            )
            Spacer(Modifier.height(16.dp))
            AuthDivider()
            Spacer(Modifier.height(16.dp))
            SocialSignInButton(
                "Sign up with Google",
                {}
            )
            Spacer(Modifier.height(24.dp))
            AuthFooterText(
                "Already have an account?",
                "Login",
                onNavigateClick
            )
        }
    }
}
