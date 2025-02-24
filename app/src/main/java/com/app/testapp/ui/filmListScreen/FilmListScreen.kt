package com.app.testapp.ui.filmListScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.testapp.models.Film
import com.app.testapp.ui.filmListScreen.component.FilmCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(viewModel: FilmListViewModel, onFilmClick: (Int) -> Unit) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearching = searchQuery.isNotBlank()

    val films = viewModel.films.collectAsLazyPagingItems()
    val searchFilms = viewModel.searchFilms.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильмы") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Поиск фильмов") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (isSearching) {
                    SearchFilmList(searchFilms) {
                        onFilmClick(it.id)
                    }
                } else {
                    FilmList(films) {
                        onFilmClick(it.id)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmList(films: LazyPagingItems<Film>, onFilmClick: (Film) -> Unit) {
    val refreshState = films.loadState.refresh
    val isRefreshing = refreshState is LoadState.Loading && films.itemCount > 0

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { films.refresh() }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Популярные фильмы",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(films.itemCount, key = { films.peek(it)?.id ?: it }) {
                        films[it]?.let { film ->
                            FilmCard(film) {
                                onFilmClick.invoke(film)
                            }
                        }
                    }

                    when (films.loadState.append) {
                        is LoadState.Loading -> {
                            item { LoadingItem() }
                        }

                        is LoadState.Error -> {
                            item {
                                ErrorAppendItem(
                                    message = "Ошибка при загрузке",
                                    onClick = {
                                        films.retry()
                                    }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        if (refreshState is LoadState.Error && films.itemCount == 0) {
            ErrorLoadData(modifier = Modifier.fillMaxSize()) { films.retry() }
        }

        if (refreshState is LoadState.Loading && films.itemCount == 0) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (refreshState !is LoadState.Error && refreshState !is LoadState.Loading && films.itemCount == 0) {
            EmptyListComponent(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun SearchFilmList(films: LazyPagingItems<Film>, onFilmClick: (Film) -> Unit) {
    val refreshState = films.loadState.refresh

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(films.itemCount, key = { films.peek(it)?.id ?: it }) {
                films[it]?.let { film ->
                    FilmCard(film) {
                        onFilmClick.invoke(film)
                    }
                }
            }

            when (films.loadState.append) {
                is LoadState.Loading -> {
                    item { LoadingItem() }
                }

                is LoadState.Error -> {
                    item {
                        ErrorAppendItem(
                            message = "Ошибка при загрузке",
                            onClick = {
                                films.retry()
                            }
                        )
                    }
                }

                else -> {}
            }

            if (refreshState !is LoadState.Error && refreshState !is LoadState.Loading && films.itemCount == 0) {
                item {
                    EmptyListComponent(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        if (refreshState is LoadState.Error && films.itemCount == 0) {
            ErrorLoadData(modifier = Modifier.fillMaxSize()) { films.retry() }
        }

        if (refreshState is LoadState.Loading && films.itemCount == 0) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

    }
}

@Composable
private fun ErrorLoadData(modifier: Modifier, onRetryClick: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Не удалось загрузить данные",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetryClick) {
            Text("Повторить")
        }
    }
}

@Composable
private fun EmptyListComponent(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Список пуст",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorAppendItem(message: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier
                .padding(16.dp),
            onClick = { onClick.invoke() },
        ) {
            Text("Повторить")
        }
    }
}