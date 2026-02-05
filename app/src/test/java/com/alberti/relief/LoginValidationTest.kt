package com.alberti.relief

import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas Unitarias Locales (Local Unit Tests) para la lógica de negocio.
 *
 * Propósito:
 * Validar algoritmos puros y reglas de negocio sin depender del framework de Android
 * ni de un emulador. Estas pruebas son rápidas y se ejecutan en la JVM local.
 *
 * Reglas de negocio validadas:
 * - Formato de correo electrónico (debe contener '@').
 * - Seguridad de contraseña (longitud mínima requerida).
 *
 * Tipos de prueba:
 * - Happy Path (Camino feliz/caso correcto).
 * - Casos de borde/error (entradas inválidas).
 */
class LoginValidationTest {

    private fun esLoginValido(correo: String, pass: String): Boolean {
        return correo.contains("@") && pass.length >= 4
    }

    @Test
    fun login_correoCorrectoYPasswordLarga_esValido() {
        val resultado = esLoginValido("usuario@test.com", "12345")
        assertTrue("Debería ser válido con datos correctos", resultado)
    }

    @Test
    fun login_correoSinArroba_esInvalido() {
        val resultado = esLoginValido("usuariotest.com", "12345")
        assertFalse("Debería fallar si no tiene @", resultado)
    }

    @Test
    fun login_passwordCorta_esInvalido() {
        val resultado = esLoginValido("usuario@test.com", "123")
        assertFalse("Debería fallar si la contraseña es menor a 4 caracteres", resultado)
    }
}