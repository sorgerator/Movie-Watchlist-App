package com.example.movie_watchlist_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_watchlist_app.presentation.ui.theme.MovieWatchlistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateBasicsScreen(onBack: () -> Unit = {}) {
    var counter by remember { mutableIntStateOf(0) }

    var textInput by rememberSaveable { mutableStateOf("") }

    var isChecked by remember { mutableStateOf(false) }

    val derivedValue by remember { derivedStateOf { counter * 2 } }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("State Basics Sandbox") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Counter: $counter", style = MaterialTheme.typography.headlineMedium)
                    Text("Derived (Counter x 2): $derivedValue", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = { counter-- }) { Text("Decrease") }
                        Button(onClick = { counter++ }) { Text("Increase") }
                    }
                }
            }

            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Type something (survives rotation)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text(if (isChecked) "Checkbox is Checked!" else "Checkbox is Unchecked")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StateBasicsScreenPreview() {
    MovieWatchlistTheme {
        StateBasicsScreen()
    }
}