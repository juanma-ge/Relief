package com.alberti.relief.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(navController: NavHostController, rol: Rol) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var textoBuscado by remember { mutableStateOf("") }
    val centrosDetectados = remember { mutableStateListOf<CentrosUrgencias>() }
    var estaCargando by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val realizarBusqueda = {
        scope.launch {
            estaCargando = true
            try {
                ejecutarBusquedaUniversal(textoBuscado, fusedLocationClient, centrosDetectados, context)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                estaCargando = false
            }
        }
    }

    val voiceLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            textoBuscado = spokenText
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            realizarBusqueda()
        } else {
            Toast.makeText(context, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relief", fontWeight = FontWeight.Bold) },
                actions = {
                    if (rol == Rol.ADMIN) {
                        IconButton(onClick = { navController.navigate("PantallaAdmin") }) {
                            Icon(Icons.Default.BarChart, "Admin")
                        }
                    }
                    IconButton(onClick = { navController.navigate("PantallaEmergencia") }) {
                        Icon(Icons.Default.Emergency, "Emergencia", tint = Color.Red)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = textoBuscado,
                onValueChange = { textoBuscado = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar sitio") },
                placeholder = { Text("Busca una ciudad o sitio específico") },
                trailingIcon = {
                    Row {
                        IconButton(onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            }
                            voiceLauncher.launch(intent)
                        }) { Icon(Icons.Default.Mic, "Voz") }

                        IconButton(onClick = {
                            permissionLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        }) { Icon(Icons.Default.Search, "Buscar") }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    textoBuscado = ""
                    permissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp)

            ) {
                Icon(Icons.Default.GpsFixed, null)
                Spacer(Modifier.width(8.dp))
                Text("BUSCAR CERCA DE MÍ", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (estaCargando) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }

            if (centrosDetectados.isNotEmpty()) {

                Text("Resultados encontrados:", fontWeight = FontWeight.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(centrosDetectados) { centro ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalHospital,
                                        tint = Color.Red,
                                        modifier = Modifier.size(32.dp),
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(centro.nombre, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                        Text(centro.direccion, fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Button(
                                    onClick = {
                                        val uri = Uri.parse("google.navigation:q=${centro.latitud},${centro.longitud}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                                            setPackage("com.google.android.apps.maps")
                                        }
                                        context.startActivity(mapIntent)
                                    },
                                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Icon(Icons.Default.Directions, null, modifier = Modifier.size(16.dp))
                                    Text(" CÓMO LLEGAR", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            } else if (!estaCargando) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.LightGray)
                    Text("No hay resultados. Pulsa buscar.", color = Color.Gray)
                }
            }
        }
    }
}
