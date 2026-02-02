package com.alberti.relief.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
                Text("Auditoría", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = { compartirInforme(context, listaAccesos) }) {
                Icon(Icons.Default.Share, contentDescription = "Exportar Informe")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.05f))
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Resultados del informe", fontSize = 12.sp)
                Text("${listaAccesos.size} registros encontrados", fontSize = 24.sp, fontWeight = FontWeight.Black)
            }
        }

        GraficoBarrasAccesos(listaAccesos)

        Spacer(modifier = Modifier.height(8.dp))

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
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

fun compartirInforme(context: Context, lista: List<AccesoEntity>) {
    val totalAdmins = lista.count { it.rol == "ADMIN" }
    val totalUsuarios = lista.count { it.rol == "USUARIO" }

    val sb = StringBuilder()
    sb.append("INFORME DE AUDITORÍA - RELIEF APP\n")
    sb.append("====================================\n\n")
    sb.append("RESUMEN ESTADÍSTICO:\n")
    sb.append("- Total registros visibles: ${lista.size}\n")
    sb.append("- Administradores: $totalAdmins\n")
    sb.append("- Usuarios estándar: $totalUsuarios\n\n")
    sb.append("DETALLE DE ACCESOS:\n")
    sb.append("------------------------------------\n")

    lista.forEach { acceso ->
        sb.append("📅 ${acceso.fecha} | 👤 ${acceso.rol}\n")
        sb.append("📧 ${acceso.correo}\n")
        sb.append("------------------------------------\n")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Informe de Accesos Relief")
        putExtra(Intent.EXTRA_TEXT, sb.toString())
    }
    context.startActivity(Intent.createChooser(intent, "Compartir informe vía..."))
}

@Composable
fun GraficoBarrasAccesos(accesos: List<AccesoEntity>) {
    val totalAdmins = accesos.count { it.rol == "ADMIN" }
    val totalUsuarios = accesos.count { it.rol == "USUARIO" }
    val maxValor = maxOf(totalAdmins, totalUsuarios, 1)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Estadísticas", fontWeight = FontWeight.Bold, color = Color.Gray)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(Color.Red, CircleShape))
                    Text(" Admin", fontSize = 10.sp, modifier = Modifier.padding(end = 8.dp))

                    Box(Modifier.size(10.dp).background(Color(0xFF1976D2), CircleShape))
                    Text(" Usuario", fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BarraIndividual(
                    cantidad = totalAdmins,
                    maximo = maxValor,
                    color = Color.Red
                )

                BarraIndividual(
                    cantidad = totalUsuarios,
                    maximo = maxValor,
                    color = Color(0xFF1976D2)
                )
            }
        }
    }
}

@Composable
fun BarraIndividual(cantidad: Int, maximo: Int, color: Color) {
    val porcentajeAltura = if (maximo > 0) cantidad.toFloat() / maximo else 0f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = cantidad.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(fraction = maxOf(porcentajeAltura, 0.02f))
                .background(color = color, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
    }
}