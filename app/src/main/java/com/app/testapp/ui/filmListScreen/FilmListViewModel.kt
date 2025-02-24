package com.app.testapp.ui.filmListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.testapp.data.FilmRepository
import com.app.testapp.models.Film
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class FilmListViewModel(
    filmRepository: FilmRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val films = filmRepository.loadFilms()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchFilms: StateFlow<PagingData<Film>> = searchQuery
        .debounce(500)
        .flatMapLatest { query ->
            if(query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                filmRepository.searchFilms(query)
            }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

@Suppress("UNCHECKED_CAST")
class FilmListViewModelFactory(private val repository: FilmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmListViewModel::class.java)) {
            return FilmListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}