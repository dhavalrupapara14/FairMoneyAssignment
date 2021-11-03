package com.fairmoney.assignment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDetailDao {

    @Query("SELECT * FROM user_detail WHERE id = :userId")
    fun getUser(userId: String): UserDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDetail)
}