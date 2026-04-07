package com.example.movie_watchlist_app.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthHeader(title: String, subtitle: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = subtitle, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
    }
}

@Composable
fun AuthTextField(value: String, onValueChange: (String) -> Unit, label: String, error: String? = null) {
    Column {
        OutlinedTextField(
            value = value, onValueChange = onValueChange, label = { Text(label) },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = error != null
        )
        if (error != null) Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun PasswordTextField(value: String, onValueChange: (String) -> Unit, label: String, error: String? = null) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = value, onValueChange = onValueChange, label = { Text(label) },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(text = if (passwordVisible) "Hide" else "Show", color = MaterialTheme.colorScheme.primary)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = error != null
        )
        if (error != null) Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun AuthPrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) { Text(text = text, color = MaterialTheme.colorScheme.onPrimary) }
}

@Composable
fun AuthDivider() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(" OR ", color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SocialSignInButton(text: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AuthFooterText(normalText: String, actionText: String, onActionClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text(text = normalText, color = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = actionText, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onActionClick() })
    }
}