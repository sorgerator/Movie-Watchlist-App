package com.example.filmfolio.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filmfolio.presentation.components.*
import com.example.filmfolio.presentation.util.AuthValidators
import androidx.compose.ui.res.stringResource
import com.example.filmfolio.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                LoginNavigationEvent.NavigateToHome -> onNavigateToHome()
                LoginNavigationEvent.NavigateToRegister -> onNavigateToRegister()
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onLoginClick = { email, password -> viewModel.onLoginClick(email, password) },
        onGoogleSignInClick = { context -> viewModel.onGoogleSignInClick(context) },
        onRegisterClick = onNavigateToRegister
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onLoginClick: (String, String) -> Unit,
    onGoogleSignInClick: (android.content.Context) -> Unit,
    onRegisterClick: () -> Unit
) {
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
                            text = uiState.message ?: stringResource(R.string.error_unknown),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    AuthPrimaryButton(
                        stringResource(R.string.btn_login),
                        onClick = {
                            hasTriedSubmit = true
                            val currentEmailError = AuthValidators.validateEmail(email)
                            val currentPasswordError = AuthValidators.validatePassword(password)
                            if (currentEmailError == null && currentPasswordError == null) {
                                onLoginClick(email.trim(), password)
                            }
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                    AuthDivider()
                    Spacer(Modifier.height(24.dp))
                    val context = androidx.compose.ui.platform.LocalContext.current
                    SocialSignInButton(
                        stringResource(R.string.btn_google_signin),
                        { onGoogleSignInClick(context) }
                    )
                    Spacer(Modifier.height(32.dp))
                    AuthFooterText(
                        stringResource(R.string.no_account),
                        stringResource(R.string.register_now),
                        onRegisterClick
                    )
                }
            }
        }
    }
}
