package com.app.testapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.testapp.data.local.models.FilmEntity

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(films: List<FilmEntity>)

    @Query("SELECT * FROM films ORDER BY insertionOrder ASC")
    fun pagingSource(): PagingSource<Int, FilmEntity>

    @Query("DELETE FROM films")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM films")
    suspend fun countFilms(): Int

    @Query("SELECT MAX(insertionOrder) FROM films")
    suspend fun getMaxInsertionOrder(): Long?
}