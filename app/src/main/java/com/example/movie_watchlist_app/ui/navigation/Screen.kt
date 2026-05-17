package com.example.movie_watchlist_app.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Watchlist : Screen("watchlist")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object AddMovie : Screen("add_movie")

    object MovieDetails : Screen("movie_details/{movieId}/{movieTitle}") {
        fun createRoute(movieId: Long, movieTitle: String) = "movie_details/$movieId/$movieTitle"
    }
}
