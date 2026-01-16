package com.alberti.relief

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alberti.relief.data.Rol
import com.alberti.relief.screen.PantallaAdmin
import com.alberti.relief.screen.PantallaEmergencia
import com.alberti.relief.screen.PantallaLogin
import com.alberti.relief.screen.PantallaPrincipal


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

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