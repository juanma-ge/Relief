package com.alberti.relief.data

/**
 * Modelos de datos de la aplicación.
 *
 * - [CentrosUrgencias]: Estructura para almacenar resultados de la API de Google Places.
 * Incluye nombre, dirección, coordenadas y la distancia calculada respecto al usuario.
 *
 * - [Rol]: Enumeración (Enum) que define los niveles de privilegios (ADMIN, USUARIO).
 *
 * - [Usuario]: Modelo transitorio para gestionar la sesión activa en memoria.
 */
data class CentrosUrgencias(
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    var distanciaReal: Float = 0f
)
