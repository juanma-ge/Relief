package com.alberti.relief.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.ui.input.pointer.pointerInput
import com.alberti.relief.components.BotonEmergencia


@Composable
fun PantallaEmergencia(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CONTACTOS DE EMERGENCIA", fontWeight = FontWeight.Black, fontSize = 22.sp)

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Red, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                        context.startActivity(intent)
                    })
                },
            contentAlignment = Alignment.Center
        ) {
            Text("SOS", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Black)
        }
        Text("Mantén pulsado para llamar al 112", color = Color.Gray, modifier = Modifier.padding(top = 10.dp))

        Spacer(modifier = Modifier.height(40.dp))

        BotonEmergencia("POLICÍA", Icons.Default.LocalPolice, Color(0xFF0D47A1), "091")
        BotonEmergencia("BOMBEROS", Icons.Default.FireTruck, Color(0xFFE65100), "080")
        BotonEmergencia("SALUD", Icons.Default.MedicalServices, Color(0xFF2E7D32), "061")
    }
}