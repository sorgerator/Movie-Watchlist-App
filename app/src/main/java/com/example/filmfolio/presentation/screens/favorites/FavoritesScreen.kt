package com.example.filmfolio.presentation.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

    FavoritesContent(
        uiState = uiState,
        onToggleWatched = { movie -> viewModel.onToggleWatched(movie) },
        onToggleFavorite = { movie -> viewModel.onToggleFavorite(movie) },
        onMovieClick = { movieId -> viewModel.onMovieClick(movieId) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesContent(
    uiState: FavoritesUiState,
    onToggleWatched: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    onMovieClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.nav_favorites).uppercase(),
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
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState) {
                is FavoritesUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
                is FavoritesUiState.Success -> {
                    if (uiState.favoriteMovies.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = "https://firebasestorage.googleapis.com/v0/b/filmfolio-app.appspot.com/o/favorites_img.jpg?alt=media",
                                contentDescription = null,
                                error = painterResource(id = R.drawable.favorites_img),
                                placeholder = painterResource(id = R.drawable.favorites_img),
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "NO FAVORITES YET".uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
                                letterSpacing = 2.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(uiState.favoriteMovies) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onToggleWatched = { onToggleWatched(movie) },
                                    onToggleFavorite = { onToggleFavorite(movie) },
                                    onDelete = { onToggleFavorite(movie) }, // Delete favors star toggle flags isFavorite = false
                                    onClick = { onMovieClick(movie.id) }
                                )
                            }
                        }
                    }
                }
                is FavoritesUiState.Error -> {
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
