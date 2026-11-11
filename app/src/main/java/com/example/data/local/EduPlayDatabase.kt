package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class EduPlayDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: EduPlayDatabase? = null

        fun getDatabase(context: Context): EduPlayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduPlayDatabase::class.java,
                    "eduplay_local_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
