package com.alberti.relief.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import com.alberti.relief.data.Stats
import com.alberti.relief.data.local.AccesoEntity
import com.alberti.relief.data.local.AppDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(navController: NavHostController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var listaAccesos by remember { mutableStateOf(listOf<AccesoEntity>()) }
    var filtroActual by remember { mutableStateOf("TODOS") }

    LaunchedEffect(filtroActual) {
        listaAccesos = if (filtroActual == "TODOS") {
            db.accesoDao().obtenerTodos()
        } else {
            db.accesoDao().filtrarPorRol(filtroActual)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) }
        Text("Panel de Auditoría", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.05f))
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Resultados del informe", fontSize = 12.sp)
                Text("${listaAccesos.size} registros encontrados", fontSize = 24.sp, fontWeight = FontWeight.Black)
            }
        }

        Text("Filtrar por tipo de usuario:", fontWeight = FontWeight.Bold)
        Row(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val opciones = listOf("TODOS", "ADMIN", "USUARIO")
            opciones.forEach { opcion ->
                FilterChip(
                    selected = filtroActual == opcion,
                    onClick = { filtroActual = opcion },
                    label = { Text(opcion) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(Modifier.fillMaxSize()) {
            items(listaAccesos) { acceso ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    ListItem(
                        headlineContent = { Text(acceso.correo, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Entró el: ${acceso.fecha}") },
                        trailingContent = {
                            Text(
                                acceso.rol,
                                color = if(acceso.rol == "ADMIN") Color.Red else Color.DarkGray,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        }
                    )
                }
            }
        }
    }
}