package com.alberti.relief.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import com.alberti.relief.data.Rol
import com.alberti.relief.data.Usuario

@Composable
fun PantallaLogin(navController: NavHostController){

    var correo by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    val usuarioCreado: (Usuario) -> Unit = { usuario ->
        navController.navigate("")
    }

    Column(
        modifier = Modifier
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(Icons.Default.HealthAndSafety, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.Red)
        Text("RELIEF", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.Red)
        Text("Tu red de emergencia", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(0.9f),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código de administradores (Opcional)")},
            modifier = Modifier.fillMaxWidth(0.7f)
        )

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                if (correo.contains("@") && contrasenia.length >= 4) {
                    val rolAsignado = if (codigo == "Admin1314") Rol.ADMIN else Rol.USUARIO
                    usuarioCreado(Usuario(correo, rolAsignado, contrasenia))
                } else {
                    mensajeError = "Email inválido o contraseña corta"
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text("Iniciar sesión")
        }

    }


}