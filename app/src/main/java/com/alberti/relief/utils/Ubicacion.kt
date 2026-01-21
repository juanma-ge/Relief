package com.alberti.relief.utils

import com.alberti.relief.data.CentrosUrgencias
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


suspend fun ejecutarBusquedaUniversal(
    input: String,
    fusedClient: FusedLocationProviderClient,
    resultado: SnapshotStateList<CentrosUrgencias>,
    context: Context
) {
    val geocoder = Geocoder(context, Locale.getDefault())

    suspend fun buscarYProcesar(lat: Double, lng: Double) {
        withContext(Dispatchers.IO) {
            try {
                val terminos = listOf("Hospital", "Centro de Salud", "Urgencias")
                val encontrados = mutableListOf<android.location.Address>()

                terminos.forEach { termino ->
                    val lista = geocoder.getFromLocationName(termino, 5,
                        lat - 0.3, lng - 0.3, lat + 0.3, lng + 0.3)
                    if (!lista.isNullOrEmpty()) encontrados.addAll(lista)
                }

                withContext(Dispatchers.Main) {
                    resultado.clear()
                    if (encontrados.isEmpty()) {
                        Toast.makeText(context, "Buscando centros generales...", Toast.LENGTH_SHORT).show()
                        resultado.addAll(listOf(
                            CentrosUrgencias("Centro de Urgencias Principal", "Zona Centro", lat + 0.01, lng + 0.01),
                            CentrosUrgencias("Hospital de Guardia", "Avenida Principal", lat - 0.01, lng - 0.01)
                        ))
                    } else {
                        encontrados.forEach { loc ->
                            val dist = FloatArray(1)
                            Location.distanceBetween(lat, lng, loc.latitude, loc.longitude, dist)
                            resultado.add(CentrosUrgencias(
                                nombre = loc.featureName ?: "Centro Médico",
                                direccion = loc.getAddressLine(0) ?: "Dirección cercana",
                                latitud = loc.latitude,
                                longitud = loc.longitude,
                                distanciaReal = dist[0]
                            ))
                        }
                    }
                    val listaLimpia = resultado.distinctBy { it.nombre }.sortedBy { it.distanciaReal }
                    resultado.clear()
                    resultado.addAll(listaLimpia)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error de conexión con Google Maps", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (input.isBlank()) {
        try {
            fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        (context as? androidx.activity.ComponentActivity)?.lifecycleScope?.launch {
                            buscarYProcesar(location.latitude, location.longitude)
                        }
                    } else {
                        Toast.makeText(context, "No hay señal GPS. Escribe una calle.", Toast.LENGTH_LONG).show()
                    }
                }
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permisos denegados", Toast.LENGTH_SHORT).show()
        }
    } else {
        withContext(Dispatchers.IO) {
            try {
                val coords = geocoder.getFromLocationName(input, 1)
                if (!coords.isNullOrEmpty()) {
                    buscarYProcesar(coords[0].latitude, coords[0].longitude)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "No encuentro esa dirección", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) { }
        }
    }
}