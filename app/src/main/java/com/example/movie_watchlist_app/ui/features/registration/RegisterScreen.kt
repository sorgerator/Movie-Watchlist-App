package com.example.movie_watchlist_app.ui.features.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.example.movie_watchlist_app.ui.components.*
import com.example.movie_watchlist_app.ui.util.AuthValidators

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasTriedSubmit by remember { mutableStateOf(false) }

    val fullNameError = if (hasTriedSubmit && fullName.isBlank()) stringResource(R.string.error_registration_failed) else null
    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                RegisterNavigationEvent.NavigateBackToLogin -> onNavigateBack()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    AuthHeader(
                        stringResource(R.string.register_account),
                        stringResource(R.string.login_subtitle) // Reusing subtitle or similar
                    )
                    Spacer(Modifier.height(32.dp))
                    
                    AuthTextField(
                        fullName,
                        { fullName = it },
                        stringResource(R.string.label_full_name),
                        fullNameError,
                        leadingIcon = Icons.Default.Person
                    )
                    Spacer(Modifier.height(16.dp))
                    
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

                    if (uiState is RegisterUiState.Error) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (uiState as RegisterUiState.Error).message ?: stringResource(R.string.error_unknown),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    AuthPrimaryButton(
                        stringResource(R.string.register_now),
                        onClick = {
                            hasTriedSubmit = true
                            if (fullNameError == null && emailError == null && passwordError == null) {
                                viewModel.onRegisterClick(fullName, email, password)
                            }
                        }
                    )
                    
                    Spacer(Modifier.height(32.dp))
                    AuthFooterText(
                        stringResource(R.string.already_have_account),
                        stringResource(R.string.btn_login),
                        onNavigateBack
                    )
                }
            }
        }
    }
}
