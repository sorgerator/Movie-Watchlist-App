package com.example.filmfolio.presentation.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.filmfolio.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.filmfolio.data.local.entity.Movie

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

    MovieDetailContent(
        uiState = uiState,
        movieId = movieId,
        onUpdateStatus = { mId, status -> viewModel.onUpdateStatus(mId, status) },
        onAddToWatchlist = { mId, status -> viewModel.onAddToWatchlist(mId, status) },
        onBack = { viewModel.onBackClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailContent(
    uiState: MovieDetailsUiState,
    movieId: Long,
    onUpdateStatus: (Long, String) -> Unit,
    onAddToWatchlist: (Long, String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.movie_details).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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
                is MovieDetailsUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
                is MovieDetailsUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = uiState.movie.title.uppercase(),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = 2.sp
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = uiState.movie.releaseDate.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                letterSpacing = 1.sp
                            )
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Text(
                            text = uiState.movie.description,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 28.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        
                        Spacer(Modifier.height(48.dp))
                        
                        if (uiState.isInWatchlist) {
                            Text(
                                text = stringResource(R.string.current_status, uiState.currentStatus ?: "").uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { onUpdateStatus(movieId, "Watched") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    stringResource(R.string.mark_as_watched).uppercase(),
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                        } else {
                            Button(
                                onClick = { onAddToWatchlist(movieId, "Plan to Watch") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    stringResource(R.string.add_to_watchlist).uppercase(),
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
                is MovieDetailsUiState.Error -> {
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
