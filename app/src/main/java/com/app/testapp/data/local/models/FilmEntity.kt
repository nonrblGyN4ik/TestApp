package com.app.testapp.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.testapp.models.Film

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey @ColumnInfo(name = "kinopoiskid") val kinopoiskid: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_url") val posterUrl: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "rating") val rating: String?,
    @ColumnInfo(name = "insertionOrder") val insertionOrder: Long
)

fun FilmEntity.toDomainModel() = Film(
    id = kinopoiskid,
    title = title,
    posterUrl = posterUrl,
    year = year,
    rating = rating.orEmpty()
)