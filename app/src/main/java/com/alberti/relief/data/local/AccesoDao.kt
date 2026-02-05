package com.alberti.relief.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Interfaz de Acceso a Datos (DAO) para la gestión del historial de accesos.
 * Room generará automáticamente la implementación de esta interfaz.
 */
@Dao
interface AccesoDao {
    @Insert
    suspend fun insertarAcceso(acceso: AccesoEntity)

    @Query("SELECT * FROM historial_accesos ORDER BY id DESC")
    suspend fun obtenerTodos(): List<AccesoEntity>

    @Query("SELECT * FROM historial_accesos WHERE rol = :rolFiltro ORDER BY id DESC")
    suspend fun filtrarPorRol(rolFiltro: String): List<AccesoEntity>
}