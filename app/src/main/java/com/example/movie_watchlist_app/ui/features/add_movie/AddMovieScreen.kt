package com.example.movie_watchlist_app.ui.features.add_movie

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.title_add_movie)) })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Add Movie Feature coming soon!")
        }
    }
}
