package com.example.movie_watchlist_app.presentation.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.model.HardcodedData
import com.example.movie_watchlist_app.presentation.navigation.Screen

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_watchlist_app.presentation.viewmodel.HomeShortcutViewModel

@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    val homeShortcutViewModel: HomeShortcutViewModel = viewModel()
    val searchTip by homeShortcutViewModel.searchTip.collectAsStateWithLifecycle()
    
    SideEffect {
        println("Dashboard search tip: $searchTip")
    }

    val categories = HardcodedData.genres

    val quickActions = listOf(
        Pair(
            "Watchlist",
            Screen.Watchlist.route
        ),
        Pair(
            "Favorites",
            Screen.Favorites.route
        ),
        Pair(
            "Add Movie",
            Screen.AddMovie.route
        ),
        Pair(
            "Profile",
            Screen.Profile.route
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.movie_banner),
                contentDescription = "Movie Dashboard Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        item {
            Text(
                "Movie Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Text("Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { Text(category) }
                    )
                }
            }
        }

        item {
            Text("Quick Access", style = MaterialTheme.typography.titleMedium)
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(quickActions) { action ->
                    Button(
                        onClick = { onNavigate(action.second) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(action.first)
                    }
                }
            }
        }

        item {
            Text("Recently Viewed", style = MaterialTheme.typography.titleMedium)
        }

        items(5) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                ListItem(
                    headlineContent = {
                        Text("Movie Placeholder ${index + 1}")
                    },
                    supportingContent = {
                        Text("Genre • Action")
                    },
                    leadingContent = {
                        Icon(Icons.Default.PlayArrow, null)
                    }
                )
            }
        }
    }
}