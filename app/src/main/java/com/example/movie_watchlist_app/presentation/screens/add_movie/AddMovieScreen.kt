package com.example.movie_watchlist_app.presentation.screens.add_movie

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun AddMovieScreen(
    onSaveClick: (String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var genre by remember { mutableStateOf("Action") }
    var priority by remember { mutableStateOf("Medium") }

    val genres = listOf("Action", "Sci-Fi", "Drama", "Comedy")
    val priorities = listOf("Low", "Medium", "High")

    AddMovieScreen(
        title = title,
        description = description,
        expanded = expanded,
        genre = genre,
        priority = priority,
        genres = genres,
        priorities = priorities,
        onTitleChange = { title = it },
        onDescriptionChange = { description = it },
        onExpandedChange = { expanded = it },
        onGenreChange = { genre = it },
        onPriorityChange = { priority = it },
        onSaveClick = {
            onSaveClick(title, genre, description, priority)
            onBack()
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMovieScreen(
    title: String,
    description: String,
    expanded: Boolean,
    genre: String,
    priority: String,
    genres: List<String>,
    priorities: List<String>,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onGenreChange: (String) -> Unit,
    onPriorityChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Movie", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            OutlinedTextField(value = title, onValueChange = onTitleChange, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = description, onValueChange = onDescriptionChange, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
                OutlinedTextField(
                    value = genre, onValueChange = {}, readOnly = true,
                    label = { Text("Genre") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                    genres.forEach { g ->
                        DropdownMenuItem(text = { Text(g) }, onClick = { onGenreChange(g); onExpandedChange(false) })
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Viewing Priority", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                priorities.forEach { p ->
                    RadioButton(
                        selected = priority == p,
                        onClick = { onPriorityChange(p) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(text = p, modifier = Modifier.padding(end = 16.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onSaveClick,
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            ) { Text("Save Movie") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMovieScreenPreview() {
    MovieWatchlistTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddMovieScreen(onSaveClick = { _, _, _, _ -> }, onBack = {})
        }
    }
}