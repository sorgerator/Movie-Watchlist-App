package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.presentation.ui.components.StatisticItem
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

@Composable
fun DashboardScreen(onNavigateToWatchlist: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Tarik Sorguč", style = MaterialTheme.typography.titleLarge)
                Text("Movie Buff Level 1", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatisticItem("Watched", "0")
            StatisticItem("Watchlist", "0")
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = onNavigateToWatchlist, modifier = Modifier.fillMaxWidth()) {
            Text("Open Watchlist")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    MovieWatchlistTheme {
        DashboardScreen(onNavigateToWatchlist = {})
    }
}