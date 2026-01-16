package com.alberti.relief.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Directions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.alberti.relief.data.CentrosUrgencias
import com.alberti.relief.data.Rol
import com.alberti.relief.data.Usuario
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(navController: NavHostController, rol: Rol) {

    var textoBuscado by remember { mutableStateOf("") }
    val centrosDetectados = remember { mutableStateListOf<CentrosUrgencias>()}
    val context = LocalContext.current

    val voiceLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            textoBuscado = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relief") },
                actions = {
                    if(rol == Rol.ADMIN) {
                        IconButton(onClick = { navController.navigate("PantallaAdmin")}) {
                            Icon(Icons.Default.BarChart, contentDescription = "Admin", tint = Color.Red)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("PantallaEmergencia")}) {
                Icon(Icons.Default.Emergency, contentDescription = "SOS", tint = Color.White)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textoBuscado,
                onValueChange = { textoBuscado = it },
                modifier = Modifier.fillMaxWidth(0.9f),
                placeholder = { Text("Buscar un centro médico") },
                trailingIcon = {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                        }
                        voiceLauncher.launch(intent)
                    }) { Icon(Icons.Default.Mic, null) }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "Detectando los sitios más cercanos...",
                        Toast.LENGTH_SHORT
                    ).show()

                    centrosDetectados.clear()
                    centrosDetectados.addAll(
                        listOf(
                            CentrosUrgencias(
                                "Hospital General Universitario",
                                "Calle Salud 1, Madrid",
                                "0.4 km"
                            ),
                            CentrosUrgencias(
                                "Centro Médico de Urgencias",
                                "Avenida de la Paz 22, Madrid",
                                "0.9 km"
                            ),
                            CentrosUrgencias(
                                "Clínica Nuestra Señora",
                                "Plaza de la Vida 5, Madrid",
                                "1.5 km"
                            )
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.GpsFixed, null)
                Spacer(Modifier.width(8.dp))
                Text("DETECTAR SITIOS CERCANOS")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (centrosDetectados.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pulsa para buscar centros de emergencia", color = Color.Gray)
                }
            } else {
                Text("Centros más cercanos: ", fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(centrosDetectados) { centro ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.LocalHospital,
                                        tint = Color.Red,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(centro.nombre, fontWeight = FontWeight.Bold)
                                        Text(centro.direccion, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(
                                        centro.distancia,
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        val gmmIntentUri =
                                            Uri.parse("google.navigation:q=${centro.direccion}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        context.startActivity(mapIntent)
                                    },
                                    modifier = Modifier.align(Alignment.End),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                                ) {
                                    Icon(Icons.Default.Directions, null, Modifier.size(18.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Cómo ir", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

            }

        }
    }

}