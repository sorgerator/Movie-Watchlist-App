package com.example.movie_watchlist_app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.presentation.util.Movie

@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
            Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
            Text(text = movie.genre, style = MaterialTheme.typography.bodyLarge)
        }
    }
}