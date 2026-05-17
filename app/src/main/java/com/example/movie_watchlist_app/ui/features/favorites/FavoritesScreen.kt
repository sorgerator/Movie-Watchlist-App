package com.example.movie_watchlist_app.ui.features.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.domain.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToDetails: (Movie) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is FavoritesNavigationEvent.NavigateToMovieDetails -> {
                    val movie = (uiState as? FavoritesUiState.Success)?.favoriteMovies?.find { it.id == event.movieId }
                    if (movie != null) onNavigateToDetails(movie)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_favorites)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is FavoritesUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is FavoritesUiState.Success -> {
                    if (state.favoriteMovies.isEmpty()) {
                        Text(
                            text = "No favorites added yet.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.favoriteMovies) { movie ->
                                ListItem(
                                    headlineContent = { Text(movie.title) },
                                    supportingContent = { Text(movie.description) },
                                    modifier = Modifier.clickable { viewModel.onMovieClick(movie.id) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
                is FavoritesUiState.Error -> {
                    Text(
                        text = state.message ?: stringResource(R.string.error_unknown),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }
}
