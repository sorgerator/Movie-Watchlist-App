package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
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
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = genre, onValueChange = {}, readOnly = true,
                    label = { Text("Genre") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    genres.forEach { g ->
                        DropdownMenuItem(text = { Text(g) }, onClick = { genre = g; expanded = false })
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Viewing Priority", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                priorities.forEach { p ->
                    RadioButton(
                        selected = priority == p,
                        onClick = { priority = p },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(text = p, modifier = Modifier.padding(end = 16.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { onSaveClick(title, genre, description, priority); onBack() },
                enabled = title.isNotBlank(), modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Save Movie") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMovieScreenPreview() {
    MovieWatchlistTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AddMovieScreen(onSaveClick = { _, _, _, _ -> }, onBack = {})
        }
    }
}