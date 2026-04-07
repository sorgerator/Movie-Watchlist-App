package com.example.movie_watchlist_app.presentation.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.components.MovieItem
import com.example.movie_watchlist_app.presentation.util.Movie
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    movies: List<Movie>,
    onToggleWatched: (Int, Boolean) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit
) {
    val favoriteMovies = movies.filter { it.isFavorite }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Favorites",
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
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (favoriteMovies.isEmpty()) {
                Text("No favorites yet. Tap the Star icon on a movie to see it here!")
            } else {
                LazyColumn {
                    items(
                        items = favoriteMovies,
                        key = {
                                movie -> movie.id
                        }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onToggleWatched = {
                                    isWatched -> onToggleWatched(
                                movie.id,
                                isWatched
                            )
                            },
                            onToggleFavorite = {
                                    isFav -> onToggleFavorite(
                                movie.id,
                                isFav
                            )
                            },
                            onDelete = {
                                onDelete(movie.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoritesScreenPreview() {
    val mockMovies = listOf(
        Movie(
            id = 1,
            title = "The Dark Knight",
            genre = "Action",
            description = "",
            priority = "High",
            isWatched = false,
            isFavorite = true
        ),
        Movie(
            id = 2,
            title = "Inception",
            genre = "Sci-Fi",
            description = "",
            priority = "Medium",
            isWatched = false,
            isFavorite = true
        )
    )

    MovieWatchlistTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FavoritesScreen(
                movies = mockMovies,
                onToggleWatched = { _, _ -> },
                onToggleFavorite = { _, _ -> },
                onDelete = { _ -> },
                onBack = {}
            )
        }
    }
}