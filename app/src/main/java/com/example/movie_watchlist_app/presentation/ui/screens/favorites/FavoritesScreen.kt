package com.example.movie_watchlist_app.presentation.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.model.Movie
import com.example.movie_watchlist_app.presentation.ui.components.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    movies: List<Movie>,
    onToggleWatched: (Int, Boolean) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit
) {
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
            if (movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No favorites yet. Tap the Star icon on a movie to see it here!")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(
                        items = movies,
                        key = {
                            it.id
                        }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onToggleWatched = {
                                onToggleWatched(
                                    movie.id,
                                    it
                                )
                            },
                            onToggleFavorite = {
                                onToggleFavorite(
                                    movie.id,
                                    it
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