package com.example.movie_watchlist_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movie_watchlist_app.presentation.navigation.NavGraph
import com.example.movie_watchlist_app.presentation.navigation.Screen
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme
import com.example.movie_watchlist_app.presentation.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieWatchlistTheme {
                MovieAppMain()
            }
        }
    }
}

@Composable
fun MovieAppMain() {
    val navController = rememberNavController()
    val movieViewModel: MovieViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(
            "Home",
            Screen.Dashboard.route,
            Icons.Default.Home
        ),
        BottomNavItem(
            "Watchlist",
            Screen.Watchlist.route,
            Icons.AutoMirrored.Filled.List
        ),
        BottomNavItem(
            "Favorites",
            Screen.Favorites.route,
            Icons.Default.Favorite
        ),
        BottomNavItem(
            "Profile",
            Screen.Profile.route,
            Icons.Default.AccountCircle
        )
    )

    val showBottomBar = bottomNavItems.any {
        it.route == currentDestination?.route
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(item.label)
                            },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navController = navController,
                movieViewModel = movieViewModel
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)