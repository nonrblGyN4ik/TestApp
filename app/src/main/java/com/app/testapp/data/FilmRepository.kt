package com.app.testapp.data

import androidx.paging.PagingData
import com.app.testapp.models.Film
import com.app.testapp.models.FilmDetails
import kotlinx.coroutines.flow.Flow

interface FilmRepository {

    fun loadFilms(): Flow<PagingData<Film>>

    suspend fun loadFilmDetails(filmId: Int): Result<FilmDetails>

    fun searchFilms(keyword: String): Flow<PagingData<Film>>
}