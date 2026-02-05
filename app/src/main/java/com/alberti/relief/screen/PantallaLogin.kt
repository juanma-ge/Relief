package com.alberti.relief.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import com.alberti.relief.data.Rol
import com.alberti.relief.data.Usuario
import com.alberti.relief.data.local.AccesoEntity
import com.alberti.relief.data.local.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Pantalla de inicio de sesión. Gestiona la autenticación básica y la asignación de roles.
 *
 * @param navController Controlador de navegación para redirigir tras login exitoso.
 * @param usuarioCreado Callback que devuelve el objeto Usuario con el rol asignado.
 */
@Composable
fun PantallaLogin(navController: NavHostController, usuarioCreado: (Usuario) -> Unit){
    var correo by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.HealthAndSafety, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.Red)
        Text("RELIEF", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.Red)
        Text("Tu red de emergencia", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código Admin (Opcional)") }, modifier = Modifier.fillMaxWidth())

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (correo.contains("@") && contrasenia.length >= 4) {
                    val rolAsignado = if (codigo == "Admin1314") Rol.ADMIN else Rol.USUARIO

                    scope.launch {
                        db.accesoDao().insertarAcceso(
                            AccesoEntity(
                                correo = correo,
                                rol = rolAsignado.name,
                                fecha = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
                            )
                        )
                    }

                    usuarioCreado(Usuario(correo, rolAsignado, contrasenia))
                } else {
                    mensajeError = "Datos incorrectos"
                }
            },

            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

        ) {
            Text("ENTRAR", fontWeight = FontWeight.Bold)
        }
    }
}