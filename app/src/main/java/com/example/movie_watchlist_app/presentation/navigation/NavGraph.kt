package com.example.movie_watchlist_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movie_watchlist_app.presentation.ui.screens.add_movie.AddMovieScreen
import com.example.movie_watchlist_app.presentation.ui.screens.dashboard.DashboardScreen
import com.example.movie_watchlist_app.presentation.ui.screens.favorites.FavoritesScreen
import com.example.movie_watchlist_app.presentation.ui.screens.login.LoginScreen
import com.example.movie_watchlist_app.presentation.ui.screens.profile.ProfileScreen
import com.example.movie_watchlist_app.presentation.ui.screens.registration.RegistrationScreen
import com.example.movie_watchlist_app.presentation.ui.screens.watchlist.WatchlistScreen
import com.example.movie_watchlist_app.presentation.ui.screens.movie_details.MovieDetailScreen
import com.example.movie_watchlist_app.presentation.viewmodel.MovieViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegistrationScreen(
                onRegisterSuccess = { navController.navigate(Screen.Login.route) },
                onNavigateClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable(Screen.Watchlist.route) {
            val movies by movieViewModel.filteredMovies.collectAsState()
            val searchQuery by movieViewModel.searchQuery.collectAsState()
            val selectedGenre by movieViewModel.selectedGenre.collectAsState()
            
            WatchlistScreen(
                movies = movies,
                searchQuery = searchQuery,
                selectedGenre = selectedGenre,
                onSearchQueryChange = { movieViewModel.onSearchQueryChange(it) },
                onGenreChange = { movieViewModel.onGenreChange(it) },
                onToggleWatched = { id, _ -> movieViewModel.toggleWatched(id) },
                onToggleFavorite = { id, _ -> movieViewModel.toggleFavorite(id) },
                onDelete = { id -> movieViewModel.deleteMovie(id) },
                onNavigateToAdd = { navController.navigate(Screen.AddMovie.route) },
                onMovieClick = { id, title -> 
                    navController.navigate(Screen.MovieDetails.createRoute(id, title))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            val favoriteMovies by movieViewModel.favoriteMovies.collectAsState()
            FavoritesScreen(
                movies = favoriteMovies,
                onToggleWatched = { id, _ -> movieViewModel.toggleWatched(id) },
                onToggleFavorite = { id, _ -> movieViewModel.toggleFavorite(id) },
                onDelete = { id -> movieViewModel.deleteMovie(id) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AddMovie.route) {
            AddMovieScreen(
                onSaveClick = { title, genre, desc, prio -> 
                    movieViewModel.addMovie(title, genre, desc, prio)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            val addedCount by movieViewModel.totalMoviesCount.collectAsState()
            val watchedCount by movieViewModel.watchedMoviesCount.collectAsState()
            val favoriteCount by movieViewModel.favoriteMoviesCount.collectAsState()
            
            ProfileScreen(
                addedCount = addedCount,
                watchedCount = watchedCount,
                favoriteCount = favoriteCount,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("movieTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: ""
            MovieDetailScreen(
                movieId = movieId,
                movieTitle = movieTitle,
                onBack = { navController.popBackStack() }
            )
        }
    }
}