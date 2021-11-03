package com.fairmoney.assignment.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_detail")
data class UserDetail(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val title: String?,
    val firstName: String?,
    val lastName: String?,
    val picture: String?,
    val gender: String?,
    val email: String?,
    val dateOfBirth: String?,
    val phone: String?
)
