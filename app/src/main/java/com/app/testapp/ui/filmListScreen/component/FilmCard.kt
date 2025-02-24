package com.app.testapp.ui.filmListScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.app.testapp.models.Film
import com.app.testapp.ui.shimmerBrush

@Composable
fun FilmCard(film: Film, onClick: (Int) -> Unit) {
    val showShimmer = remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(film.id) }
    ) {
        Row {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        shimmerBrush(
                            targetValue = 1000f,
                            showShimmer = showShimmer.value
                        )
                    ),
                model = film.posterUrl,
                contentDescription = null,
                onSuccess = { showShimmer.value = false },
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Год: ${film.year}")
                Text(text = "Рейтинг: ${film.rating}")
            }
        }
    }
}