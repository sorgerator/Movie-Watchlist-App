package com.example.filmfolio.presentation.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.filmfolio.R

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToWatchlist: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                DashboardNavigationEvent.Logout -> onLogout()
                DashboardNavigationEvent.NavigateToProfile -> onNavigateToProfile()
                else -> {}
            }
        }
    }

    DashboardContent(
        uiState = uiState,
        onLogoutClick = { viewModel.onLogoutClick() },
        onNavigateToWatchlist = onNavigateToWatchlist,
        onNavigateToFavorites = onNavigateToFavorites,
        onNavigateToProfile = onNavigateToProfile
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onLogoutClick: () -> Unit,
    onNavigateToWatchlist: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_dashboard).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 3.sp,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.cd_logout),
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is DashboardUiState.Error -> {
                    Text(
                        text = uiState.message ?: stringResource(R.string.error_unknown),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                else -> {
                    // Success or Init
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Top Remote Banner with Coil & fallback
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(24.dp))
                        ) {
                            AsyncImage(
                                model = "https://firebasestorage.googleapis.com/v0/b/filmfolio-app.appspot.com/o/movie_banner.jpg?alt=media",
                                contentDescription = "Movie Banner",
                                error = painterResource(id = R.drawable.movie_banner),
                                placeholder = painterResource(id = R.drawable.movie_banner),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.8f)
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "FILMFOLIO HUB",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.labelMedium,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "DISCOVER YOUR NEXT FAVORITE FILM",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    style = MaterialTheme.typography.headlineSmall,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Text(
                            text = "QUICK NAVIGATION",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Three clear card-like links
                        NavigationCard(
                            title = "MY WATCHLIST",
                            imageUrl = "https://firebasestorage.googleapis.com/v0/b/filmfolio-app.appspot.com/o/watchlist_img.jpg?alt=media",
                            fallbackImageRes = R.drawable.watchlist_img,
                            onClick = onNavigateToWatchlist
                        )

                        NavigationCard(
                            title = "FAVORITE MOVIES",
                            imageUrl = "https://firebasestorage.googleapis.com/v0/b/filmfolio-app.appspot.com/o/favorites_img.jpg?alt=media",
                            fallbackImageRes = R.drawable.favorites_img,
                            onClick = onNavigateToFavorites
                        )

                        NavigationCard(
                            title = "MY PROFILE",
                            imageUrl = "https://firebasestorage.googleapis.com/v0/b/filmfolio-app.appspot.com/o/profile_img.jpg?alt=media",
                            fallbackImageRes = R.drawable.profile_img,
                            onClick = onNavigateToProfile
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationCard(
    title: String,
    imageUrl: String,
    fallbackImageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                error = painterResource(id = fallbackImageRes),
                placeholder = painterResource(id = fallbackImageRes),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.8f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
