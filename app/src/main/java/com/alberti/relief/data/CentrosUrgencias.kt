package com.alberti.relief.data

data class CentrosUrgencias(
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    var distanciaReal: Float = 0f
)
