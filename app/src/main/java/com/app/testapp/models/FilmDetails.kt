package com.app.testapp.models

data class FilmDetails(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val description: String,
    val genres: List<Genre>,
    val countries: List<Country>
)

data class Genre(
    val genre: String
)

data class Country(
    val country: String
)