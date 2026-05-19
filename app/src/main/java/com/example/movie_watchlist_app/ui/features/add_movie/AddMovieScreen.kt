package com.example.movie_watchlist_app.ui.features.add_movie

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.movie_watchlist_app.R

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.movie_watchlist_app.ui.components.AuthPrimaryButton
import com.example.movie_watchlist_app.ui.components.AuthTextField
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(
    viewModel: AddMovieViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var releaseDate by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                AddMovieNavigationEvent.NavigateBack -> onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_add_movie).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClick() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthTextField(
                value = title,
                onValueChange = { title = it },
                label = stringResource(R.string.label_movie_title),
                leadingIcon = Icons.Default.Title
            )
            
            AuthTextField(
                value = genre,
                onValueChange = { genre = it },
                label = stringResource(R.string.label_genre),
                leadingIcon = Icons.Default.Category
            )

            AuthTextField(
                value = releaseDate,
                onValueChange = { releaseDate = it },
                label = stringResource(R.string.label_release_date),
                leadingIcon = Icons.Default.CalendarToday
            )

            AuthTextField(
                value = priority,
                onValueChange = { priority = it },
                label = stringResource(R.string.label_priority),
                leadingIcon = Icons.Default.PriorityHigh
            )

            AuthTextField(
                value = description,
                onValueChange = { description = it },
                label = stringResource(R.string.label_description),
                leadingIcon = Icons.Default.Description
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthPrimaryButton(
                text = stringResource(R.string.btn_save_movie),
                enabled = title.isNotBlank() && description.isNotBlank(),
                onClick = {
                    viewModel.onAddMovie(title, description, genre, priority, releaseDate)
                }
            )
        }
    }
}

