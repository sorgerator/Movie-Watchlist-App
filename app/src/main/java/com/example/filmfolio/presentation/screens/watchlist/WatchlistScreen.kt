package com.example.filmfolio.presentation.screens.watchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.filmfolio.R
import com.example.filmfolio.data.local.entity.Movie
import com.example.filmfolio.presentation.components.MovieCard
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    onNavigateToDetails: (Movie) -> Unit,
    onNavigateToAddMovie: () -> Unit,
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
                WatchlistNavigationEvent.NavigateBack -> onBack()
            }
        }
    }

    WatchlistContent(
        uiState = uiState,
        onToggleWatched = { movie -> viewModel.onToggleWatched(movie) },
        onToggleFavorite = { movie -> viewModel.onToggleFavorite(movie) },
        onRemoveClick = { movieId -> viewModel.onRemoveClick(movieId) },
        onMovieClick = { movieId -> viewModel.onMovieClick(movieId) },
        onNavigateToAddMovie = onNavigateToAddMovie,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistContent(
    uiState: WatchlistUiState,
    onToggleWatched: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    onRemoveClick: (Long) -> Unit,
    onMovieClick: (Long) -> Unit,
    onNavigateToAddMovie: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_watchlist).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddMovie,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.title_add_movie))
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState) {
                is WatchlistUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
                is WatchlistUiState.Success -> {
                    if (uiState.watchlist.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = "https://firebasestorage.googleapis.com/v0/b/filmfolio-app.appspot.com/o/watchlist_img.jpg?alt=media",
                                contentDescription = null,
                                error = painterResource(id = R.drawable.watchlist_img),
                                placeholder = painterResource(id = R.drawable.watchlist_img),
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.watchlist_empty).uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
                                letterSpacing = 2.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(uiState.watchlist) { item ->
                                MovieCard(
                                    movie = item.movie,
                                    onToggleWatched = { onToggleWatched(item.movie) },
                                    onToggleFavorite = { onToggleFavorite(item.movie) },
                                    onDelete = { onRemoveClick(item.movie.id) },
                                    onClick = { onMovieClick(item.movie.id) }
                                )
                            }
                        }
                    }
                }
                is WatchlistUiState.Error -> {
                    Text(
                        text = uiState.message ?: stringResource(R.string.error_unknown),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                else -> {}
            }
        }
    }
}
