package com.app.testapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.testapp.data.api.KinopoiskApi
import com.app.testapp.data.local.AppDatabase
import com.app.testapp.data.local.models.FilmEntity
import com.app.testapp.data.local.models.RemoteKeys
import com.app.testapp.data.models.toEntityModel

const val FILM_REMOTE_KEY_LABEL = "filmRemoteKeyLabel"

@OptIn(ExperimentalPagingApi::class)
class FilmRemoteMediator(
    private val api: KinopoiskApi,
    private val db: AppDatabase
) : RemoteMediator<Int, FilmEntity>() {

    private val filmDao = db.filmDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val cacheIsEmpty = filmDao.countFilms() == 0

        return if (cacheIsEmpty) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FilmEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val keys = getNextRemoteKey()
                    keys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getPopularFilms(page)
            val maxOrder = filmDao.getMaxInsertionOrder() ?: 0L
            val films =
                response.items.mapIndexed { index, film -> film.toEntityModel(maxOrder + index + 1) }

            val isReachEnd = response.items.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.delete(FILM_REMOTE_KEY_LABEL)
                    filmDao.clearAll()
                }

                val nextKey =
                    RemoteKeys(
                        label = FILM_REMOTE_KEY_LABEL,
                        //оставил возможность запросить последнюю страницу при достижении конца списка, на случай если там появились данные
                        //если вместо page добавить null, то при достижении конца списка загрузка стартовать никогда не будет
                        nextKey = if (isReachEnd) page else page + 1
                    )
                remoteKeysDao.insertOrReplace(nextKey)

                filmDao.insertAll(films)
            }

            MediatorResult.Success(endOfPaginationReached = isReachEnd)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getNextRemoteKey(): RemoteKeys? {
        return remoteKeysDao.getRemoteKeys(FILM_REMOTE_KEY_LABEL)
    }
}