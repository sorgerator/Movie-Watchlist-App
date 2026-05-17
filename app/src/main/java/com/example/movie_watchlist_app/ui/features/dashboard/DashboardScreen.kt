package com.example.movie_watchlist_app.ui.features.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToDetails: (Movie) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is DashboardNavigationEvent.NavigateToMovieDetails -> {
                    val movie = (uiState as? DashboardUiState.Success)?.movies?.find { it.id == event.movieId }
                    if (movie != null) onNavigateToDetails(movie)
                }
                DashboardNavigationEvent.Logout -> onLogout()
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_dashboard)) },
                actions = {
                    IconButton(onClick = { viewModel.onLogoutClick() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = stringResource(R.string.cd_logout))
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is DashboardUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.movies) { movie ->
                            ListItem(
                                headlineContent = { Text(movie.title) },
                                supportingContent = { Text(movie.description) },
                                modifier = Modifier.clickable { viewModel.onMovieClick(movie.id) }
                            )
                            HorizontalDivider()
                        }
                    }
                }
                is DashboardUiState.Error -> {
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
