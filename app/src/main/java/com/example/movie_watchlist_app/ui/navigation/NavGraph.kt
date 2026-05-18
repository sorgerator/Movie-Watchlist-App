package com.example.movie_watchlist_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movie_watchlist_app.ui.features.login.LoginViewModel
import com.example.movie_watchlist_app.ui.features.login.LoginScreen
import com.example.movie_watchlist_app.ui.features.registration.RegisterViewModel
import com.example.movie_watchlist_app.ui.features.registration.RegisterScreen
import com.example.movie_watchlist_app.ui.features.dashboard.DashboardViewModel
import com.example.movie_watchlist_app.ui.features.dashboard.DashboardScreen
import com.example.movie_watchlist_app.ui.features.watchlist.WatchlistViewModel
import com.example.movie_watchlist_app.ui.features.watchlist.WatchlistScreen
import com.example.movie_watchlist_app.ui.features.favorites.FavoritesViewModel
import com.example.movie_watchlist_app.ui.features.favorites.FavoritesScreen
import com.example.movie_watchlist_app.ui.features.profile.ProfileViewModel
import com.example.movie_watchlist_app.ui.features.profile.ProfileScreen
import com.example.movie_watchlist_app.ui.features.add_movie.AddMovieScreen
import com.example.movie_watchlist_app.ui.features.details.MovieDetailsViewModel
import com.example.movie_watchlist_app.ui.features.details.MovieDetailScreen

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToDetails = { movie ->
                    val encodedTitle = URLEncoder.encode(movie.title, StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.MovieDetails.createRoute(movie.id, encodedTitle))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Watchlist.route) {
            val viewModel: WatchlistViewModel = hiltViewModel()
            WatchlistScreen(
                viewModel = viewModel,
                onNavigateToDetails = { movie ->
                    val encodedTitle = URLEncoder.encode(movie.title, StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.MovieDetails.createRoute(movie.id, encodedTitle))
                },
                onBack = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = hiltViewModel()
            FavoritesScreen(
                viewModel = viewModel,
                onNavigateToDetails = { movie ->
                    val encodedTitle = URLEncoder.encode(movie.title, StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.MovieDetails.createRoute(movie.id, encodedTitle))
                },
                onBack = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddMovie.route) {
            AddMovieScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.LongType },
                navArgument("movieTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getLong("movieId") ?: 0L
            val viewModel: MovieDetailsViewModel = hiltViewModel()
            MovieDetailScreen(
                viewModel = viewModel,
                movieId = movieId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
