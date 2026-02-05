package com.alberti.relief.Navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alberti.relief.screen.PantallaAdmin
import com.alberti.relief.screen.PantallaEmergencia
import com.alberti.relief.screen.PantallaLogin
import com.alberti.relief.screen.PantallaPrincipal

/**
 * Gestor de navegación principal de la aplicación.
 *
 * Propósito:
 * Define el grafo de navegación (NavGraph) y gestiona las transiciones entre pantallas.
 * Utiliza [NavHost] para intercambiar los composables según la ruta actual.
 *
 * Rutas definidas:
 * - "Login": Pantalla inicial de autenticación.
 * - "Principal/{rol}": Pantalla principal que recibe el ROL como argumento dinámico.
 * - "PantallaAdmin": Panel de auditoría (acceso restringido por lógica de negocio).
 * - "PantallaEmergencia": Acceso rápido a teléfonos de socorro.
 *
 * @see NavHostController
 */
@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login") {

        composable("Login") {
            PantallaLogin(
                navController = navController,
                usuarioCreado = { usuario ->
                    navController.navigate("Principal/${usuario.rol.name}")
                }
            )
        }

        composable(
            route = "Principal/{rol}",
            arguments = listOf(navArgument("rol") { type = NavType.StringType })
        ) { backStackEntry ->
            val rolStr = backStackEntry.arguments?.getString("rol") ?: "USUARIO"
            val rolActual = if (rolStr == "ADMIN") Rol.ADMIN else Rol.USUARIO
            PantallaPrincipal(navController, rolActual)
        }

        composable("PantallaAdmin") {
            PantallaAdmin(navController)
        }

        composable("PantallaEmergencia") {
            PantallaEmergencia(navController)
        }
    }

}