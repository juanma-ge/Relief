package com.alberti.relief.utils

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.alberti.relief.data.CentrosUrgencias
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Locale

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
suspend fun ejecutarBusquedaUniversal(
    textoBuscado: String,
    fusedClient: FusedLocationProviderClient,
    resultado: SnapshotStateList<CentrosUrgencias>,
    context: Context
) {
    try {
        val (lat, lng) = if (textoBuscado.isBlank()) {
            val location = fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await() ?: run {
                withContext(Dispatchers.Main) { Toast.makeText(context, "GPS apagado o sin señal", Toast.LENGTH_SHORT).show() }
                return
            }
            location.latitude to location.longitude
        } else {
            val geocoder = Geocoder(context, Locale.getDefault())
            val direcciones = withContext(Dispatchers.IO) {
                geocoder.getFromLocationName(textoBuscado, 1)
            }
            if (direcciones.isNullOrEmpty()) {
                withContext(Dispatchers.Main) { Toast.makeText(context, "No encontré esa dirección", Toast.LENGTH_SHORT).show() }
                return
            }
            direcciones[0].latitude to direcciones[0].longitude
        }

        withContext(Dispatchers.Main) { resultado.clear() }

        val listaHospitales = buscarEnGoogle(context, lat, lng, "hospital")
        val listaFarmacias = buscarEnGoogle(context, lat, lng, "pharmacy")

        val total = (listaHospitales + listaFarmacias).sortedBy { it.distanciaReal }

        withContext(Dispatchers.Main) {
            if (total.isEmpty()) {
                Toast.makeText(context, "No hay sitios cerca en 10km", Toast.LENGTH_LONG).show()
            } else {
                resultado.addAll(total.take(10)) // Mostramos hasta 10
            }
        }

    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Log.e("RELIEF_ERROR", "Error: ${e.message}")
            Toast.makeText(context, "Error crítico: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}

private suspend fun buscarEnGoogle(
    context: Context,
    lat: Double,
    lng: Double,
    tipo: String
): List<CentrosUrgencias> = withContext(Dispatchers.IO) {
    val tempLista = mutableListOf<CentrosUrgencias>()

    val apiKey = context.getString(com.alberti.relief.R.string.google_maps_key)

    val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat,$lng&radius=10000&type=$tipo&key=$apiKey"

    try {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val jsonData = response.body?.string() ?: ""
        val json = JSONObject(jsonData)

        val status = json.getString("status")

        if (status == "OK") {
            val results = json.getJSONArray("results")
            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val loc = item.getJSONObject("geometry").getJSONObject("location")
                val latDest = loc.getDouble("lat")
                val lngDest = loc.getDouble("lng")

                val dist = FloatArray(1)
                Location.distanceBetween(lat, lng, latDest, lngDest, dist)

                tempLista.add(CentrosUrgencias(
                    nombre = item.getString("name"),
                    direccion = item.optString("vicinity", "Ver en mapa"),
                    latitud = latDest,
                    longitud = lngDest,
                    distanciaReal = dist[0]
                ))
            }
        } else if (status != "ZERO_RESULTS") {
            withContext(Dispatchers.Main) {
                //Log.d("RELIEF_API", "Status: $status. Msg: ${json.optString("error_message")}")
                //Toast.makeText(context, "Google dice: $status", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        Log.e("RELIEF_API", "Fallo de red: ${e.message}")
    }

    return@withContext tempLista
}