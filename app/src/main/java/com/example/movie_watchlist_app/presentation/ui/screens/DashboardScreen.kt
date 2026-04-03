package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.presentation.ui.components.ShortcutCard
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

data class DashboardItem(val title: String, val subtitle: String, val icon: ImageVector, val route: String, val imageRes: Int)

@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    val items = listOf(
        DashboardItem("My Watchlist", "View saved movies", Icons.Default.List, "watchlist", R.drawable.watchlist_img),
        DashboardItem("Add Movie", "Discover new titles", Icons.Default.Add, "add_movie", R.drawable.add_movie_img),
        DashboardItem("Favorites", "Top rated picks", Icons.Default.Star, "favorites", R.drawable.favorites_img),
        DashboardItem("Profile", "Account settings", Icons.Default.AccountCircle, "profile", R.drawable.profile_img)
    )

    val filtered = items.filter { it.title.contains(searchQuery, ignoreCase = true) }.chunked(2)

    DashboardScreen(
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        filteredRows = filtered,
        onNavigate = onNavigate
    )
}

@Composable
private fun DashboardScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filteredRows: List<List<DashboardItem>>,
    onNavigate: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.movie_banner),
            contentDescription = "Movie Dashboard Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(16.dp))

        Text("Movie Dashboard", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery, onValueChange = onSearchQueryChange,
            label = { Text("Search menus") }, leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(16.dp))

        filteredRows.forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ShortcutCard(
                    title = rowItems[0].title, subtitle = rowItems[0].subtitle, icon = rowItems[0].icon,
                    imageRes = rowItems[0].imageRes, onClick = { onNavigate(rowItems[0].route) }, modifier = Modifier.weight(1f)
                )
                if (rowItems.size > 1) {
                    ShortcutCard(
                        title = rowItems[1].title, subtitle = rowItems[1].subtitle, icon = rowItems[1].icon,
                        imageRes = rowItems[1].imageRes, onClick = { onNavigate(rowItems[1].route) }, modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MovieWatchlistTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            DashboardScreen(onNavigate = {})
        }
    }
}