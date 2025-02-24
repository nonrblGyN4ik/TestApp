package com.app.testapp.data.models

import com.app.testapp.models.Country
import com.app.testapp.models.FilmDetails
import com.app.testapp.models.Genre
import com.google.gson.annotations.SerializedName

data class FilmDetailsDto(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("countries") val countries: List<CountryDto>?
)

data class GenreDto(
    @SerializedName("genre") val genre: String?
)

data class CountryDto(
    @SerializedName("country") val country: String?
)


fun FilmDetailsDto.toDomainModel() = FilmDetails(
    id = id,
    title = title.orEmpty(),
    posterUrl = posterUrl.orEmpty(),
    description = description.orEmpty(),
    genres = genres?.map { it.toDomainModel() }.orEmpty(),
    countries = countries?.map { it.toDomainModel() }.orEmpty()
)

fun GenreDto.toDomainModel() = Genre(
    genre = genre.orEmpty()
)

fun CountryDto.toDomainModel() = Country(
    country = country.orEmpty()
)