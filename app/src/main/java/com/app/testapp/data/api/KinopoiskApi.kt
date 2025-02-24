package com.app.testapp.data.api

import com.app.testapp.data.models.FilmDetailsDto
import com.app.testapp.data.models.FilmListResponse
import com.app.testapp.data.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("/api/v2.2/films")
    suspend fun getPopularFilms(@Query("page") page: Int): FilmListResponse

    @GET("/api/v2.2/films/{id}")
    suspend fun getFilmDetails(@Path("id") id: Int): FilmDetailsDto

    @GET("/api/v2.1/films/search-by-keyword")
    suspend fun searchFilms(
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): SearchResponse
}


