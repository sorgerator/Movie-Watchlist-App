package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.presentation.ui.components.MovieCard
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme
import com.example.movie_watchlist_app.presentation.util.Movie

@Composable
fun WatchlistScreen(
    movies: List<Movie>,
    onNavigateToAdd: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) { Text("+") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            if (movies.isEmpty()) {
                Text(text = stringResource(id = R.string.empty_list))
            } else {
                LazyColumn {
                    items(movies) { movie ->
                        MovieCard(movie = movie)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WatchlistScreenEmptyPreview() {
    MovieWatchlistTheme {
        WatchlistScreen(
            movies = emptyList(),
            onNavigateToAdd = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WatchlistScreenPopulatedPreview() {
    val mockMovies = listOf(
        Movie(id = 1, title = "Inception", genre = "Sci-Fi", description = ""),
        Movie(id = 2, title = "The Godfather", genre = "Crime", description = ""),
        Movie(id = 3, title = "The Dark Knight", genre = "Action", description = "")
    )

    MovieWatchlistTheme {
        WatchlistScreen(
            movies = mockMovies,
            onNavigateToAdd = {}
        )
    }
}