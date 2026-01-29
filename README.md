# RELIEF - Tu Red de Emergencia

**Relief** es una aplicación nativa Android desarrollada en **Jetpack Compose**, Kotlin como Proyecto Final de la asignatura de Desarrollo de Interfaces. El objetivo de la aplicación es mantener la seguridad del usuario, ayudándolo con los contactos de emergencia, pero especialmente, el uso de la geolocalización para buscar centros de salud cercanos a tu ubicación o en otra deseada, medinte comandos de voz y accesos rápidos a servicios de emergencia con una API.

## Contenido del Repositorio

Para facilitar la revisión y evaluación del proyecto, la documentación la he dividido en:

1.  📘 **[MANUAL DE USUARIO](./MANUAL_USUARIO.md)** *Guía visual sobre cómo utilizar la aplicación, acceder como administrador y realizar búsquedas.*

2.  ⚙️ **[JUSTIFICACIÓN TÉCNICA DE RAs](./JUSTIFICACION_RAS.md)** *Documento técnico donde se justifica punto por punto el cumplimiento de los Resultados de Aprendizaje (RA1 - RA8) con enlaces directos al código fuente.*

## Tecnologías Utilizadas

* **Lenguaje:** Kotlin & Jetpack Compose.
* **Base de Datos:** Room Database para auditoría de accesos y persistencia de datos.
* **Conectividad:** Retrofit/OkHttp para Google Places API.
* **Hardware:** GPS (FusedLocation), Micrófono (Speech Recognizer).
* **Arquitectura:** MVVM Clean Architecture (Simplificada).

## Despliegue

1.  Clona este repositorio.
2.  La `API_KEY` está en  el archivo `local.properties`, por lo que no neceitarás añadir nada.
3.  Descarga la APK que dejaré abajo, ahí se situa la app.

---
*Juan Manuel Gómez Estudillo 2 DAM -- Desarollo de Interfaces*
