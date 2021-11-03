package com.fairmoney.assignment.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_keys")
data class UserRemoteKey(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nextKey: Int?
)