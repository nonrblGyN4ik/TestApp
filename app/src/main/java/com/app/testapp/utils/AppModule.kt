package com.app.testapp.utils

import android.content.Context
import androidx.room.Room
import com.app.testapp.data.FilmRepositoryImpl
import com.app.testapp.data.api.KinopoiskApi
import com.app.testapp.data.local.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Ручное создание зависимостей, так как в ТЗ указано, что не нужен DI
object AppModule {
    private lateinit var applicationContext: Context

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }

    private val kinopoiskService = RetrofitModule.kinopoisService

    val dataBase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "film-database")
            .build()
    }

    val filmRepository by lazy { FilmRepositoryImpl(kinopoiskService, dataBase) }
}

private object RetrofitModule {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("X-API-KEY", "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    private val retrofitClient = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val kinopoisService = retrofitClient.create(KinopoiskApi::class.java)
}