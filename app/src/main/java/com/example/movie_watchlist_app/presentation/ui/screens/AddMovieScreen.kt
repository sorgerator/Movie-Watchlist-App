package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

@Composable
fun AddMovieScreen(
    onSaveClick: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    val isFormValid = title.isNotBlank() && genre.isNotBlank()

    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))

        Button(
            onClick = {
                onSaveClick(title, genre)
                onBack()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Movie")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddMovieScreenPreview() {
    MovieWatchlistTheme {
        AddMovieScreen(
            onSaveClick = { _, _ -> },
            onBack = {}
        )
    }
}