package com.example.movie_watchlist_app.presentation.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.ui.components.StatisticItem
import com.example.movie_watchlist_app.presentation.ui.screens.profile.componenets.PreferenceItem
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme

@Composable
fun ProfileScreen(onLogout: () -> Unit, onBack: () -> Unit) {
    var notificationsEnabled by remember {
        mutableStateOf(true)
    }
    var darkThemeEnabled by remember {
        mutableStateOf(false)
    }

    ProfileScreen(
        notificationsEnabled = notificationsEnabled,
        darkThemeEnabled = darkThemeEnabled,
        onNotificationsChange = { notificationsEnabled = it },
        onThemeChange = { darkThemeEnabled = it },
        onLogout = onLogout,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    notificationsEnabled: Boolean,
    darkThemeEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "User Profile",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Student Developer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "student@ibu.edu.ba",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatisticItem(label = "Added", value = "12")
                        StatisticItem(label = "Watched", value = "5")
                        StatisticItem(label = "Favorites", value = "3")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PreferenceItem(
                    icon = Icons.Default.Notifications,
                    title = "Push Notifications",
                    isChecked = notificationsEnabled,
                    onCheckedChange = onNotificationsChange
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                PreferenceItem(
                    icon = Icons.Default.Settings,
                    title = "Dark Mode",
                    isChecked = darkThemeEnabled,
                    onCheckedChange = onThemeChange
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(
                    "Logout",
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MovieWatchlistTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileScreen(
                onLogout = {},
                onBack = {}
            )
        }
    }
}