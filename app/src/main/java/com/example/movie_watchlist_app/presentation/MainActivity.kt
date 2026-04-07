package com.example.movie_watchlist_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movie_watchlist_app.presentation.ui.MovieViewModel
import com.example.movie_watchlist_app.presentation.ui.screens.*
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieWatchlistTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MovieAppNavigation()
                }
            }
        }
    }
}

@Composable
fun MovieAppNavigation() {
    val navController = rememberNavController()
    val movieViewModel: MovieViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegistrationScreen(
                onRegisterSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable("dashboard") {
            DashboardScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable("watchlist") {
            WatchlistScreen(
                movies = movieViewModel.movies,
                onToggleWatched = { id, isWatched -> movieViewModel.toggleWatched(id, isWatched) },
                onToggleFavorite = { id, isFav -> movieViewModel.toggleFavorite(id, isFav) },
                onDelete = { id -> movieViewModel.deleteMovie(id) },
                onNavigateToAdd = { navController.navigate("add_movie") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("add_movie") {
            AddMovieScreen(
                onSaveClick = { title, genre, desc, prio -> movieViewModel.addMovie(title, genre, desc, prio) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("favorites") {
            FavoritesScreen(
                movies = movieViewModel.movies,
                onToggleWatched = { id, isWatched -> movieViewModel.toggleWatched(id, isWatched) },
                onToggleFavorite = { id, isFav -> movieViewModel.toggleFavorite(id, isFav) },
                onDelete = { id -> movieViewModel.deleteMovie(id) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}