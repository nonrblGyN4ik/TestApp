package com.app.testapp.data.models

import com.app.testapp.models.Film
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("pagesCount") val pagesCount: Int,
    @SerializedName("searchFilmsCountResult") val searchFilmsCountResult: Int,
    @SerializedName("films") val films: List<SearchFilmDto>
)

data class SearchFilmDto(
    @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("rating") val rating: String?
)

fun SearchFilmDto.toDomainModel() = Film(
    id = id,
    title = title.orEmpty(),
    posterUrl = posterUrl.orEmpty(),
    year = year.orEmpty(),
    rating = rating.orEmpty()
)