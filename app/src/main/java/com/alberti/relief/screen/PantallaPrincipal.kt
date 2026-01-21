package com.alberti.relief.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.alberti.relief.data.CentrosUrgencias
import com.alberti.relief.data.Rol
import com.alberti.relief.utils.ejecutarBusquedaUniversal
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(navController: NavHostController, rol: Rol) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var textoBuscado by remember { mutableStateOf("") }
    val centrosDetectados = remember { mutableStateListOf<CentrosUrgencias>() }
    var estaCargando by remember { mutableStateOf(false) } // Para el feedback visual

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val voiceLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            textoBuscado = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.all { it.value }) {
            scope.launch {
                estaCargando = true
                ejecutarBusquedaUniversal(textoBuscado, fusedLocationClient, centrosDetectados, context)
                estaCargando = false
            }
        } else {
            Toast.makeText(context, "Se necesitan permisos de ubicación", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Relief", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()
        ) {
            OutlinedTextField(
                value = textoBuscado,
                onValueChange = { textoBuscado = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Dirección o ciudad (ej: Madrid)") },
                trailingIcon = {
                    Row {
                        IconButton(onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            }
                            voiceLauncher.launch(intent)
                        }) { Icon(Icons.Default.Mic, "Voz") }

                        IconButton(onClick = {
                            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                        }) { Icon(Icons.Default.Search, "Buscar") }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    textoBuscado = ""
                    permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Default.GpsFixed, null)
                Spacer(Modifier.width(8.dp))
                Text("BUSCAR CERCA DE MÍ")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (estaCargando) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }

            if (centrosDetectados.isNotEmpty()) {
                Text("Los 2 más cercanos:", fontWeight = FontWeight.Black)
                LazyColumn {
                    items(centrosDetectados.take(2)) { centro ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalHospital, tint = Color.Red, modifier = Modifier.size(32.dp), contentDescription = null)
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(centro.nombre, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                                        Text(centro.direccion, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text("${"%.2f".format(centro.distanciaReal / 1000)} km", color = Color.Red, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = {
                                        val uri = Uri.parse("google.navigation:q=${centro.latitud},${centro.longitud}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply { setPackage("com.google.android.apps.maps") }
                                        context.startActivity(mapIntent)
                                    },
                                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                ) {
                                    Icon(Icons.Default.Directions, null, modifier = Modifier.size(16.dp))
                                    Text(" CÓMO LLEGAR")
                                }
                            }
                        }
                    }
                }
            } else if (!estaCargando) {
                Text("Pulsa buscar para encontrar centros", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}