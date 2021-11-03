package com.fairmoney.assignment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: UserRemoteKey)

    @Query("SELECT * FROM user_remote_keys LIMIT :limit")
    suspend fun getUserRemoteKey(limit: Int = 1): UserRemoteKey

    @Query("DELETE FROM user_remote_keys")
    suspend fun clearRemoteKey()
}