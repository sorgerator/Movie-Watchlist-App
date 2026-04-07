package com.example.movie_watchlist_app.presentation.screens.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.tooling.preview.Preview
import com.example.movie_watchlist_app.R
import com.example.movie_watchlist_app.presentation.theme.MovieWatchlistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "$title image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShortcutCardPreview() {
    MovieWatchlistTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ShortcutCard(
                title = "My Watchlist",
                subtitle = "View saved movies",
                icon = Icons.Default.List,
                imageRes = R.drawable.movie_banner,
                onClick = {}
            )
        }
    }
}