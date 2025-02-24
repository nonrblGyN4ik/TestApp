package com.app.testapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.testapp.data.local.models.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeys: RemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE label = :label")
    suspend fun getRemoteKeys(label: String): RemoteKeys

    @Query("DELETE FROM remote_keys WHERE label = :label")
    suspend fun delete(label: String)
}