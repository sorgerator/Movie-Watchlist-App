package com.example.movie_watchlist_app

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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movie_watchlist_app.ui.navigation.NavGraph
import com.example.movie_watchlist_app.ui.navigation.Screen
import com.example.movie_watchlist_app.ui.theme.MovieWatchlistTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

@AndroidEntryPoint
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // In a real app, we'd check session here and redirect if needed
    // For now, let's just fix the NavGraph arguments and flow
    val bottomNavItems = listOf(
        BottomNavItem(
            R.string.nav_home,
            Screen.Dashboard.route,
            Icons.Default.Home
        ),
        BottomNavItem(
            R.string.nav_watchlist,
            Screen.Watchlist.route,
            Icons.AutoMirrored.Filled.List
        ),
        BottomNavItem(
            R.string.nav_favorites,
            Screen.Favorites.route,
            Icons.Default.Favorite
        ),
        BottomNavItem(
            R.string.nav_profile,
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
                        val label = stringResource(item.labelResId)
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = label
                                )
                            },
                            label = {
                                Text(label)
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
                navController = navController
            )
        }
    }
}

data class BottomNavItem(
    val labelResId: Int,
    val route: String,
    val icon: ImageVector
)
