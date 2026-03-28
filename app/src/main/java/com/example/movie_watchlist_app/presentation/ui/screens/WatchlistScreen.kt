package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.ui.components.MovieItem
import com.example.movie_watchlist_app.presentation.util.Movie
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    movies: List<Movie>,
    onToggleWatched: (Int, Boolean) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    onNavigateToAdd: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Watchlist", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (movies.isEmpty()) {
                Text("No movies yet. Tap + to add some!")
            } else {
                LazyColumn {
                    items(movies) { movie ->
                        MovieItem(
                            movie = movie,
                            onToggleWatched = { isWatched -> onToggleWatched(movie.id, isWatched) },
                            onToggleFavorite = { isFav -> onToggleFavorite(movie.id, isFav) },
                            onDelete = { onDelete(movie.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WatchlistScreenPopulatedPreview() {
    val mockMovies = listOf(
        Movie(
            id = 1,
            title = "Inception",
            genre = "Sci-Fi",
            description = "",
            priority = "High",
            isFavorite = true,
            isWatched = false
        ),
        Movie(
            id = 2,
            title = "The Godfather",
            genre = "Crime",
            description = "",
            priority = "High",
            isFavorite = false,
            isWatched = false
        ),
        Movie(
            id = 3,
            title = "The Dark Knight",
            genre = "Action",
            description = "",
            priority = "Medium",
            isFavorite = true,
            isWatched = false,
        ),
        Movie(
            id = 3,
            title = "Goldfinger",
            genre = "Action",
            description = "",
            priority = "Medium",
            isFavorite = false,
            isWatched = false,
        ),
        Movie(
            id = 2,
            title = "No Man's Land",
            genre = "Action",
            description = "",
            priority = "Low",
            isWatched = false,
            isFavorite = false
        )
    )

    MovieWatchlistTheme {
        WatchlistScreen(
            movies = mockMovies,
            onToggleWatched = { _, _ -> },
            onToggleFavorite = { _, _ -> },
            onDelete = { _ -> },
            onNavigateToAdd = {},
            onBack = {}
        )
    }
}