package com.alberti.relief

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alberti.relief.data.local.AccesoDao
import com.alberti.relief.data.local.AccesoEntity
import com.alberti.relief.data.local.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Suite de pruebas instrumentadas para la capa de persistencia de datos (Room Database).
 *
 * Propósito:
 * Verificar que las operaciones CRUD (Crear, Leer) contra la base de datos local
 * funcionan correctamente en un entorno Android simulado.
 *
 * Características de la prueba:
 * - Utiliza una base de datos en memoria (inMemoryDatabaseBuilder) para no persistir basura.
 * - Verifica la integridad de los datos insertados.
 * - Incluye una prueba de estrés (carga masiva) para asegurar el rendimiento.
 *
 * @see AccesoDao
 * @see AppDatabase
 */
@RunWith(AndroidJUnit4::class)
class AccesoDaoTest {

    private lateinit var accesoDao: AccesoDao
    private lateinit var db: AppDatabase

    @Before
    fun crearBaseDeDatos() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        accesoDao = db.accesoDao()
    }

    @After
    @Throws(IOException::class)
    fun cerrarBaseDeDatos() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun escribirUsuarioYLeerlo() = runBlocking {
        val acceso = AccesoEntity(correo = "prueba@relief.com", rol = "ADMIN", fecha = "29/01/2026")
        accesoDao.insertarAcceso(acceso)

        val lista = accesoDao.obtenerTodos()

        assertEquals(1, lista.size)
        assertEquals("prueba@relief.com", lista[0].correo)
    }

    @Test
    fun pruebaDeCargaMasiva() = runBlocking {
        val tiempoInicio = System.currentTimeMillis()

        for (i in 1..1000) {
            accesoDao.insertarAcceso(
                AccesoEntity(correo = "user$i@test.com", rol = "USUARIO", fecha = "29/01/2026")
            )
        }

        val lista = accesoDao.obtenerTodos()
        val tiempoFin = System.currentTimeMillis()

        assertEquals(1000, lista.size)

        val duracion = tiempoFin - tiempoInicio
        assertTrue("La inserción fue demasiado lenta: ${duracion}ms", duracion < 3000)
    }
}