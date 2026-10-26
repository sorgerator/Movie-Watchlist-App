package com.example.filmfolio.presentation.screens.add_movie

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.filmfolio.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.filmfolio.presentation.components.AuthPrimaryButton
import com.example.filmfolio.presentation.components.AuthTextField
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip

@Composable
fun AddMovieScreen(
    viewModel: AddMovieViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvents) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                AddMovieNavigationEvent.NavigateBack -> onBack()
            }
        }
    }

    AddMovieContent(
        uiState = uiState,
        onAddMovie = { title, description, genre, priority, releaseDate ->
            viewModel.onAddMovie(title, description, genre, priority, releaseDate)
        },
        onBack = { viewModel.onBackClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieContent(
    uiState: AddMovieUiState,
    onAddMovie: (String, String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var releaseDate by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()

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
                    IconButton(onClick = onBack) {
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
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_movie_img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))

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

                if (uiState is AddMovieUiState.Error) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                AuthPrimaryButton(
                    text = stringResource(R.string.btn_save_movie),
                    enabled = title.isNotBlank() && description.isNotBlank() && uiState !is AddMovieUiState.Loading,
                    onClick = {
                        onAddMovie(title, description, genre, priority, releaseDate)
                    }
                )
            }

            if (uiState is AddMovieUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
