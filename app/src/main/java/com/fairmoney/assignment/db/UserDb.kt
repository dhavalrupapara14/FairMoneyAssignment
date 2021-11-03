package com.fairmoney.assignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, UserRemoteKey::class, UserDetail::class], version = 1, exportSchema = false)
abstract class UserDb : RoomDatabase() {
    companion object {
        fun create(context: Context): UserDb {
            return Room.databaseBuilder(context, UserDb::class.java, "user.db")
                .fallbackToDestructiveMigration()
//                .addMigrations(MIGRATION_1_2)
                .build()
        }

        /*private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'users' ADD COLUMN 'gender' TEXT")
                database.execSQL("ALTER TABLE 'users' ADD COLUMN 'email' TEXT")
            }
        }*/
    }

    abstract fun userDao(): UserDao
    abstract fun userRemoteKeyDao(): UserRemoteKeyDao
    abstract fun userDetailDao(): UserDetailDao
}