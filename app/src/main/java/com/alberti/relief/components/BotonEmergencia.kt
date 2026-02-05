package com.alberti.relief.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Componente UI reutilizable para botones de llamada de emergencia.
 *
 * Propósito:
 * Encapsular el diseño visual y la lógica de llamada telefónica para mantener
 * el código de las pantallas limpio y consistente.
 *
 * Comportamiento:
 * Al hacer clic, genera un Intent implícito [Intent.ACTION_DIAL] con el número proporcionado.
 *
 * @param text Etiqueta del botón (ej: "POLICÍA").
 * @param icon Icono vectorial descriptivo.
 * @param color Color de fondo del botón para distinguir la urgencia.
 * @param numero Número de teléfono al que se llamará.
 */
@Composable
fun BotonEmergencia(text: String, icon: ImageVector, color: Color, numero: String) {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numero"))
            context.startActivity(intent)
        },

        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(65.dp)

    ) {

        Icon(icon, null)
        Spacer(Modifier.width(12.dp))
        Text("$text ($numero)", fontWeight = FontWeight.Bold)

    }
}