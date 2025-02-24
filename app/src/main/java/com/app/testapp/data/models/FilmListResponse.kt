package com.app.testapp.data.models

import com.app.testapp.data.local.models.FilmEntity
import com.google.gson.annotations.SerializedName

data class FilmListResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("items") val items: List<FilmDto>
)

data class FilmDto(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("nameOriginal") val nameOriginal: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("ratingKinopoisk") val rating: String?
)


fun FilmDto.toEntityModel(insertionOrder: Long) = FilmEntity(
    kinopoiskid = id,
    title = title ?: nameOriginal.orEmpty(),
    posterUrl = posterUrl.orEmpty(),
    year = year.orEmpty(),
    rating = rating.orEmpty(),
    insertionOrder = insertionOrder
)