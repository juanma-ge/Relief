package com.alberti.relief.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase base de la base de datos Room.
 *
 * Propósito:
 * Actuar como el punto de acceso principal a la persistencia de datos SQLite.
 *
 * Patrón de Diseño:
 * Implementa el patrón **Singleton** para asegurar que solo exista una instancia
 * de la base de datos abierta al mismo tiempo, lo cual es costoso en recursos.
 *
 * Entidades registradas:
 * - [AccesoEntity]
 *
 * @see AccesoDao
 */
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