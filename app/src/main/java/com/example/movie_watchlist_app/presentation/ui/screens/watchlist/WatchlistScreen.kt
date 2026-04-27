package com.example.movie_watchlist_app.presentation.ui.screens.watchlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.model.Movie
import com.example.movie_watchlist_app.presentation.ui.components.MovieItem
import com.example.movie_watchlist_app.model.HardcodedData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    movies: List<Movie>,
    searchQuery: String,
    selectedGenre: String,
    onSearchQueryChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onToggleWatched: (Int, Boolean) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    onNavigateToAdd: () -> Unit,
    onMovieClick: (Int, String) -> Unit,
    onBack: () -> Unit
) {
    val genres = listOf("All") + HardcodedData.genres

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                "My Watchlist",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Search movies") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))

            Text(
                "Filter by Genre",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(genres) { genre ->
                    FilterChip(
                        selected = selectedGenre == genre,
                        onClick = {
                            onGenreChange(genre)
                        },
                        label = {
                            Text(genre)
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            if (movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No movies found matching your criteria.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = movies,
                        key = { it.id }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onToggleWatched = {
                                onToggleWatched(movie.id, it)
                            },
                            onToggleFavorite = {
                                onToggleFavorite(movie.id, it)
                            },
                            onDelete = {
                                onDelete(movie.id)
                            },
                            onClick = {
                                onMovieClick(movie.id, movie.title)
                            }
                        )
                    }
                }
            }
        }
    }
}