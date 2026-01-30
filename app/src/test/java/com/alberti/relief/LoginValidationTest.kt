package com.alberti.relief

import org.junit.Test
import org.junit.Assert.*

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