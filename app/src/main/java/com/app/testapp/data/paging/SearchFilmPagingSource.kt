package com.app.testapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.testapp.data.api.KinopoiskApi
import com.app.testapp.data.models.toDomainModel
import com.app.testapp.models.Film
import java.util.concurrent.ConcurrentHashMap

class SearchFilmPagingSource(
    private val api: KinopoiskApi,
    private val keyword: String
) : PagingSource<Int, Film>() {

    private val seenFilmIds = ConcurrentHashMap.newKeySet<Int>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        return try {
            val page = params.key ?: 1
            val response = api.searchFilms(keyword = keyword, page = page)
            val films = response.films.filter { seenFilmIds.add(it.id) }.map { it.toDomainModel() }

            val nextKey = if (page < response.pagesCount) {
                page + 1
            } else {
                null
            }

            LoadResult.Page(
                data = films,
                nextKey = nextKey,
                prevKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}