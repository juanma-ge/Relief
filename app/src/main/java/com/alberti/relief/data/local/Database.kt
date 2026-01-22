package com.alberti.relief.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AccesoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accesoDao(): AccesoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "relief_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}