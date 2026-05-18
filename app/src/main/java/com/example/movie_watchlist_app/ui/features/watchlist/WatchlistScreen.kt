package com.example.movie_watchlist_app.ui.features.watchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    onNavigateToDetails: (Movie) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is WatchlistNavigationEvent.NavigateToMovieDetails -> {
                    val movie = (uiState as? WatchlistUiState.Success)?.watchlist?.find { it.movie.id == event.movieId }?.movie
                    if (movie != null) onNavigateToDetails(movie)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_watchlist)) },
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
                is WatchlistUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is WatchlistUiState.Success -> {
                    if (state.watchlist.isEmpty()) {
                        Text(stringResource(R.string.watchlist_empty), modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.watchlist) { item ->
                                ListItem(
                                    headlineContent = { Text(item.movie.title) },
                                    supportingContent = { Text(stringResource(R.string.current_status, item.status)) },
                                    trailingContent = {
                                        IconButton(onClick = { viewModel.onRemoveClick(item.movie.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.cd_remove))
                                        }
                                    },
                                    modifier = Modifier.clickable { viewModel.onMovieClick(item.movie.id) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
                is WatchlistUiState.Error -> {
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
