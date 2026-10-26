package com.example.filmfolio.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.filmfolio.presentation.screens.login.LoginViewModel
import com.example.filmfolio.presentation.screens.login.LoginScreen
import com.example.filmfolio.presentation.screens.registration.RegisterViewModel
import com.example.filmfolio.presentation.screens.registration.RegisterScreen
import com.example.filmfolio.presentation.screens.dashboard.DashboardViewModel
import com.example.filmfolio.presentation.screens.dashboard.DashboardScreen
import com.example.filmfolio.presentation.screens.watchlist.WatchlistViewModel
import com.example.filmfolio.presentation.screens.watchlist.WatchlistScreen
import com.example.filmfolio.presentation.screens.favorites.FavoritesViewModel
import com.example.filmfolio.presentation.screens.favorites.FavoritesScreen
import com.example.filmfolio.presentation.screens.profile.ProfileViewModel
import com.example.filmfolio.presentation.screens.profile.ProfileScreen
import com.example.filmfolio.presentation.screens.add_movie.AddMovieViewModel
import com.example.filmfolio.presentation.screens.add_movie.AddMovieScreen
import com.example.filmfolio.presentation.screens.details.MovieDetailsViewModel
import com.example.filmfolio.presentation.screens.details.MovieDetailScreen

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
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
                onNavigateToWatchlist = { navController.navigate(Screen.Watchlist.route) },
                onNavigateToFavorites = { navController.navigate(Screen.Favorites.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
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
                onNavigateToAddMovie = {
                    navController.navigate(Screen.AddMovie.route)
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
            val viewModel: AddMovieViewModel = hiltViewModel()
            AddMovieScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
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
