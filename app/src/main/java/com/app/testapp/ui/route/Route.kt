package com.app.testapp.ui.route

sealed class Route(val path: String) {
    object FilmList : Route("film_list")
    object FilmDetail : Route("film_detail/{filmId}")
}