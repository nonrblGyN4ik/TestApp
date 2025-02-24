package com.app.testapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.testapp.data.local.models.FilmEntity
import com.app.testapp.data.local.models.RemoteKeys

@Database(entities = [FilmEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}