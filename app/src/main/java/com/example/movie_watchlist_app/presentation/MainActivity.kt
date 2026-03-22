package com.example.movie_watchlist_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
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
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MovieAppNavigation()
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun MovieAppNavigation() {
    val navController = rememberNavController()
    val movieViewModel: MovieViewModel = viewModel()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(onNavigateToWatchlist = {
                navController.navigate("watchlist")
            })
        }
        composable("watchlist") {
            WatchlistScreen(
                movies = movieViewModel.movies,
                onNavigateToAdd = { navController.navigate("add_movie") }
            )
        }
        composable("add_movie") {
            AddMovieScreen(
                onSaveClick = { title, genre ->
                    movieViewModel.addMovie(title, genre)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}