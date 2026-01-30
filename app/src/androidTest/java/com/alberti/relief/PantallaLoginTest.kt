package com.alberti.relief

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.alberti.relief.screen.PantallaLogin
import org.junit.Rule
import org.junit.Test

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