package com.example.movie_watchlist_app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_watchlist_app.model.Movie

@Composable
fun MovieItem(
    movie: Movie,
    onToggleWatched: (Boolean) -> Unit,
    onToggleFavorite: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = movie.isWatched,
                onCheckedChange = onToggleWatched,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (movie.isWatched) Color.Gray else Color.Unspecified
                )
                Text(
                    text = "${movie.genre} • ${movie.priority} Priority",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = { onToggleFavorite(!movie.isFavorite) }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint = if (movie.isFavorite) Color(0xFFFFC107) else Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MovieItem(
        movie = Movie(
            1,
            "Inception",
            "Sci-Fi",
            "Dream within a dream",
            "High",
            true,
            true
        ),
        onToggleWatched = {},
        onToggleFavorite = {},
        onDelete = {}
    )
}