package com.example.filmfolio.presentation.screens.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.filmfolio.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.example.filmfolio.presentation.components.*
import com.example.filmfolio.presentation.util.AuthValidators

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                RegisterNavigationEvent.NavigateBackToLogin -> onNavigateBack()
            }
        }
    }

    RegisterContent(
        uiState = uiState,
        onRegisterClick = { fullName, email, password ->
            viewModel.onRegisterClick(fullName, email, password)
        },
        onBack = onNavigateBack
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onRegisterClick: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasTriedSubmit by remember { mutableStateOf(false) }

    val fullNameError = if (hasTriedSubmit && fullName.isBlank()) stringResource(R.string.error_registration_failed) else null
    val emailError = if (hasTriedSubmit) AuthValidators.validateEmail(email) else null
    val passwordError = if (hasTriedSubmit) AuthValidators.validatePassword(password) else null

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
                        stringResource(R.string.login_subtitle)
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
                            text = uiState.message ?: stringResource(R.string.error_unknown),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    AuthPrimaryButton(
                        stringResource(R.string.register_now),
                        onClick = {
                            hasTriedSubmit = true
                            val currentFullNameError = if (fullName.isBlank()) "Error" else null
                            val currentEmailError = AuthValidators.validateEmail(email)
                            val currentPasswordError = AuthValidators.validatePassword(password)
                            if (currentFullNameError == null && currentEmailError == null && currentPasswordError == null) {
                                onRegisterClick(fullName.trim(), email.trim(), password)
                            }
                        }
                    )
                    
                    Spacer(Modifier.height(32.dp))
                    AuthFooterText(
                        stringResource(R.string.already_have_account),
                        stringResource(R.string.btn_login),
                        onBack
                    )
                }
            }
        }
    }
}
