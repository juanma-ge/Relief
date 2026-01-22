package com.alberti.relief.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial_accesos")
data class AccesoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val correo: String,
    val rol: String,
    val fecha: String
)