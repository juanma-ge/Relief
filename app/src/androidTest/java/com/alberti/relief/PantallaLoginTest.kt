package com.alberti.relief

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.alberti.relief.screen.PantallaLogin
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas de Interfaz de Usuario (UI) para la pantalla de Login.
 *
 * Propósito:
 * Asegurar que los componentes visuales críticos son renderizados correctamente
 * y son visibles para el usuario final.
 *
 * Herramientas:
 * Utiliza [ComposeTestRule] para acceder al árbol de nodos de la interfaz declarativa.
 *
 * Escenarios cubiertos:
 * - Visibilidad del título y subtítulo de la app.
 * - Existencia de los campos de entrada (Correo, Contraseña).
 * - Visibilidad del botón de acción principal.
 *
 * @see com.alberti.relief.screen.PantallaLogin
 */
class PantallaLoginTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificarElementosVisiblesLogin() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            PantallaLogin(
                navController = navController,
                usuarioCreado = {}
            )
        }

        composeTestRule.onNodeWithText("RELIEF").assertIsDisplayed()

        composeTestRule.onNodeWithText("Tu red de emergencia").assertIsDisplayed()

        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()

        composeTestRule.onNodeWithText("ENTRAR").assertIsDisplayed()
    }
}