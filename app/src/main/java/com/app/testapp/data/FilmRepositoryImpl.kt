package com.app.testapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.app.testapp.data.api.KinopoiskApi
import com.app.testapp.data.local.AppDatabase
import com.app.testapp.data.local.models.toDomainModel
import com.app.testapp.data.models.toDomainModel
import com.app.testapp.data.paging.FilmRemoteMediator
import com.app.testapp.data.paging.SearchFilmPagingSource
import com.app.testapp.models.Film
import com.app.testapp.models.FilmDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class FilmRepositoryImpl(
    private val api: KinopoiskApi,
    private val db: AppDatabase
) : FilmRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun loadFilms(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 2
            ),
            remoteMediator = FilmRemoteMediator(api, db),
            pagingSourceFactory = { db.filmDao().pagingSource() }
        ).flow.map { it.map { it.toDomainModel() } }
    }

    override fun searchFilms(keyword: String): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 2,
            ),
            pagingSourceFactory = { SearchFilmPagingSource(api, keyword) }
        ).flow
    }

    override suspend fun loadFilmDetails(filmId: Int): Result<FilmDetails> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.getFilmDetails(filmId).toDomainModel())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}