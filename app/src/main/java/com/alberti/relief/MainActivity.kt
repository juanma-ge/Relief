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
import com.alberti.relief.Navigation.AppNavigation
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

