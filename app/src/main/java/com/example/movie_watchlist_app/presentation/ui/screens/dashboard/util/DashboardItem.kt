package com.example.movie_watchlist_app.presentation.ui.screens.dashboard.util

import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val route: String,
    val imageRes: Int
)
