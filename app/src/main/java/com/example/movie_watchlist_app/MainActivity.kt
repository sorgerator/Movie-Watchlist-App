package com.example.movie_watchlist_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
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
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    bottomNavItems.forEach { item ->
                        val label = stringResource(item.labelResId)
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true
                        
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = label,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                                )
                            },
                            label = {
                                Text(
                                    label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.surface // No visible indicator
                            )
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
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
