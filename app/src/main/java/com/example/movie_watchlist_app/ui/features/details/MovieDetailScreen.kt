package com.example.movie_watchlist_app.ui.features.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailsViewModel,
    movieId: Long,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                MovieDetailsNavigationEvent.NavigateBack -> onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.movie_details)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is MovieDetailsUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is MovieDetailsUiState.Success -> {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                        Text(state.movie.title, style = MaterialTheme.typography.headlineLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(R.string.release_date, state.movie.releaseDate))
                        Spacer(Modifier.height(8.dp))
                        Text(state.movie.description)
                        Spacer(Modifier.height(16.dp))
                        
                        if (state.isInWatchlist) {
                            Text(stringResource(R.string.current_status, state.currentStatus ?: ""))
                            Button(onClick = { viewModel.onUpdateStatus(movieId, "Watched") }) {
                                Text(stringResource(R.string.mark_as_watched))
                            }
                        } else {
                            Button(onClick = { viewModel.onAddToWatchlist(movieId, "Plan to Watch") }) {
                                Text(stringResource(R.string.add_to_watchlist))
                            }
                        }
                    }
                }
                is MovieDetailsUiState.Error -> {
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
