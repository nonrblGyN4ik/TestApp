package com.app.testapp.ui.filmDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.testapp.data.FilmRepository
import com.app.testapp.models.FilmDetails
import com.app.testapp.ui.filmDetailScreen.models.FilmDetailsUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FilmDetailsState {
    object Loading : FilmDetailsState()
    data class Success(val filmDetails: FilmDetailsUi) : FilmDetailsState()
    data class Error(val message: String) : FilmDetailsState()
}

class FilmDetailViewModel(
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FilmDetailsState>(FilmDetailsState.Loading)
    val state: StateFlow<FilmDetailsState> = _state.asStateFlow()

    fun loadFilmDetail(filmId: Int) {
        viewModelScope.launch {
            _state.value = FilmDetailsState.Loading

            val result = filmRepository.loadFilmDetails(filmId)

            result.onSuccess {
                _state.value = FilmDetailsState.Success(mapToUiModel(it))
            }.onFailure {
                _state.value = FilmDetailsState.Error("Не удалось загрузить данные")
            }
        }
    }

    private fun mapToUiModel(filmDetails: FilmDetails): FilmDetailsUi {
        return FilmDetailsUi(
            title = filmDetails.title,
            posterUrl = filmDetails.posterUrl,
            description = filmDetails.description.ifEmpty { "Не указано" },
            genres = filmDetails.genres.map { it.genre }.joinToString(", "),
            countries = filmDetails.countries.map { it.country }.joinToString(", ")
        )
    }
}

class FilmDetailViewModelFactory(private val filmRepository: FilmRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmDetailViewModel(filmRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}