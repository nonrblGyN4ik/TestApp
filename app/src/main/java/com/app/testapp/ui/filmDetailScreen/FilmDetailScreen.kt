package com.app.testapp.ui.filmDetailScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.app.testapp.ui.filmDetailScreen.models.FilmDetailsUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    filmId: Int,
    viewModel: FilmDetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(filmId) {
        viewModel.loadFilmDetail(filmId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Подробнее о фильме") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                is FilmDetailsState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is FilmDetailsState.Success -> {
                    val filmDetails = (state as FilmDetailsState.Success).filmDetails
                    Content(filmDetails)
                }

                is FilmDetailsState.Error -> {
                    ErrorComponent(
                        modifier = Modifier.fillMaxSize(),
                        message = (state as FilmDetailsState.Error).message,
                        onRetryClick = {
                            viewModel.loadFilmDetail(filmId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(filmDetails: FilmDetailsUi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)

    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            model = filmDetails.posterUrl,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = filmDetails.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Описание: ${filmDetails.description}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Жанры: ${filmDetails.genres}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Страны: ${filmDetails.countries}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ErrorComponent(modifier: Modifier, message: String, onRetryClick: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetryClick) {
            Text("Повторить")
        }
    }
}