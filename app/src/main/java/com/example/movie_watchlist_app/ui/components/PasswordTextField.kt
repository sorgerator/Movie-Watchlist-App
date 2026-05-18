package com.example.movie_watchlist_app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            supportingText = { if (error != null) Text(error) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = onVisibilityToggle) {
                    Icon(imageVector = image, contentDescription = stringResource(R.string.cd_toggle_password))
                }
            }
        )
    }
}