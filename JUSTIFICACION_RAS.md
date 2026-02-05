# JUSTIFICACIÓN TÉCNICA DE RESULTADOS DE APRENDIZAJE (RA)

Este documento detalla exhaustivamente el cumplimiento de los criterios de evaluación del módulo de Desarrollo de Interfaces. Se justifica cada decisión técnica tomada durante el desarrollo de **Relief**, evidenciando el uso de código profesional y estándares actuales.

## 🔹 RA1. Desarrollo de la Interfaz Gráfica (GUI)

### RA1.a Analiza herramientas y librerías
**Justificación:**
Se ha realizado un análisis técnico previo para seleccionar el stack tecnológico más eficiente. Se ha optado por **Jetpack Compose (BOM)** como framework de UI por su naturaleza declarativa frente al imperativo XML, y se han integrado librerías especializadas como `maps-compose` para la gestión geoespacial y `Material3` para asegurar el cumplimiento de las guías de diseño actuales de Google.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/build.gradle.kts#L60
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/build.gradle.kts#L64
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/build.gradle.kts#L76
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/build.gradle.kts#L84-L87

### RA1.b Crea interfaz gráfica
**Justificación:**
La aplicación se estructura bajo el patrón **Single Activity Architecture**. La `MainActivity` actúa como el único punto de entrada al sistema, delegando inmediatamente la construcción de la interfaz al bloque `setContent`. Esto permite gestionar todo el ciclo de vida de la aplicación de forma centralizada y moderna, eliminando la necesidad de múltiples Activities pesadas.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/MainActivity.kt#L10-L23

<img width="200" height="800" alt="image" src="https://github.com/user-attachments/assets/41f58122-4300-464a-bbbc-b73036607581" />

### RA1.c Uso de layouts y posicionamiento
**Justificación:**
Se ha hecho un uso avanzado de los layouts de composición para estructurar la información. Se utiliza `Scaffold` para proveer la estructura visual estándar (barras superiores, contenido), `Column/Row` para la disposición lineal de elementos y, crucialmente, `LazyColumn` para renderizar listas de datos (hospitales, logs) de manera eficiente, reciclando vistas para no saturar la memoria del dispositivo.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L76-L96
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaAdmin.kt#L103-L117

### RA1.d Personalización de componentes
**Justificación:**
No se han utilizado los componentes con su apariencia por defecto. Se ha aplicado una personalización profunda mediante modificadores (`Modifier`) para alterar formas, colores y tamaños. Un ejemplo claro es el botón de SOS, que se ha diseñado como un contenedor `Box` circular con un color rojo semántico específico, diferenciándolo visualmente del resto de la interfaz para destacar su función crítica.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaEmergencia.kt#L48-L52

### RA1.e Análisis del código
**Justificación:**
El código fuente demuestra un análisis previo de arquitectura, organizándose en paquetes funcionales que separan responsabilidades. Se distingue claramente la capa de presentación (`screen`), la capa de datos y persistencia (`data/local`) y la capa de navegación (`Navigation`), lo que facilita la escalabilidad y el mantenimiento del proyecto a largo plazo.

<img width="206" height="143" alt="image" src="https://github.com/user-attachments/assets/25c1c4c9-5b6e-4d6d-912c-d36be7dbbac3" />

### RA1.f Modificación del código
**Justificación:**
La estructura basada en funciones `@Composable` permite la modificación granular de la interfaz. Cada componente visual (como un botón o una tarjeta) está encapsulado en su propia función, lo que significa que se pueden realizar cambios en el diseño o la lógica de un elemento específico sin riesgo de romper el funcionamiento de otras partes de la pantalla.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/components/BotonEmergencia.kt#L23-L27

### RA1.g Asociación de eventos
**Justificación:**
La interactividad de la aplicación se gestiona asociando eventos del usuario (como clics o escritura) a funciones lógicas mediante expresiones lambda. Esto desacopla la interfaz de la lógica de negocio; por ejemplo, el botón de login no realiza la acción directamente, sino que invoca una función de validación y creación de usuario basada en el estado actual de los campos de texto.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaLogin.kt#L84-L87

### RA1.h App integrada
**Justificación:**
Todas las pantallas y funcionalidades de la aplicación (Login, Mapas, Emergencia, Admin) están integradas en un flujo de navegación coherente gestionado por `NavHostController`. Esto permite al usuario moverse entre secciones, pasar argumentos (como el rol del usuario) y volver atrás sin perder el estado de la aplicación, garantizando una experiencia de usuario fluida.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/Navigation/AppNavigation.kt#L82-L84
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/Navigation/AppNavigation.kt#L93-L96

---

## 🔹 RA2. Mecanismos de Interacción Natural (NUI)

### RA2.a Herramientas NUI
**Justificación:**
Se han identificado e implementado herramientas de Interacción Natural de Usuario (NUI) disponibles en el ecosistema Android. Concretamente, se utiliza el sistema de `Intents` para invocar el reconocimiento de voz (`RecognizerIntent`) y las APIs de puntero de Compose (`PointerInput`) para gestionar gestos táctiles complejos, enriqueciendo la experiencia más allá de los botones tradicionales.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L108-L110

### RA2.b Diseño conceptual NUI
**Justificación:**
El diseño de la aplicación está conceptualizado para situaciones de alto estrés (emergencias). Por ello, se priorizan mecanismos NUI como botones de gran tamaño y comandos de voz, asumiendo que el usuario podría tener dificultades motoras o visuales momentáneas que le impidan escribir en un teclado pequeño, facilitando así el acceso a la ayuda.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaEmergencia.kt#L48-L50

### RA2.c Interacción por voz
**Justificación:**
Se ha implementado una funcionalidad completa de control por voz para la búsqueda de centros. Mediante `ActivityResultContracts`, la aplicación captura la voz del usuario, la transcribe a texto y ejecuta automáticamente la búsqueda del hospital o ciudad, eliminando la barrera de entrada que supone escribir texto en situaciones urgentes.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L60-L65

### RA2.d Interacción por gesto
**Justificación:**
Para evitar llamadas accidentales a servicios críticos como el 112, se ha implementado un gesto de seguridad: la pulsación larga (`onLongPress`). El usuario debe mantener presionado el botón conscientemente para activar la llamada, lo cual es un uso avanzado de la pantalla táctil que mejora la usabilidad y previene errores.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaEmergencia.kt#L48-L62

### RA2.f Realidad aumentada (Info Geolocalizada)
**Justificación:**
La aplicación utiliza la API de Google Maps para proporcionar una experiencia de realidad mixta/aumentada funcional. Al detectar un centro de salud, la aplicación puede lanzar una intención de navegación que superpone las indicaciones direccionales y la información del lugar sobre el mapa del mundo real, guiando al usuario físicamente hasta el destino. Y a pesar de que no sea realidad aumentada del todo, también se podría añadir otra opción, además de que ver el camino, de mostrarlo mediante la cámara de tu teléfono móvil para ver el camino de llegada en la realidad.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L182-L183

---

## 🔹 RA3. Componentes Visuales

### RA3.a Herramientas de componentes
**Justificación:**
Se ha seleccionado la librería oficial `androidx.compose.material3` como caja de herramientas principal. Esto proporciona acceso a una amplia gama de componentes pre-diseñados (Botones, Tarjetas, Campos de texto) que cumplen rigurosamente con los estándares de accesibilidad y diseño visual de Android, asegurando una base sólida.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/build.gradle.kts#L64

### RA3.b Componentes reutilizables
**Justificación:**
Para mantener el código limpio (DRY - Don't Repeat Yourself), se ha encapsulado la interfaz de los botones de emergencia en un componente propio llamado `BotonEmergencia`. Este componente se define una sola vez y se reutiliza para generar los botones de Policía, Bomberos y Salud, garantizando consistencia visual.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/components/BotonEmergencia.kt#L23-L27

### RA3.c Parámetros y defaults
**Justificación:**
El componente reutilizable `BotonEmergencia` está diseñado para ser flexible mediante el uso de parámetros. Acepta argumentos como el texto, el icono, el color y el número de teléfono, lo que permite que el mismo bloque de código se comporte y se vea de manera diferente según el contexto en el que se utilice.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/components/BotonEmergencia.kt#L24

### RA3.d Eventos en componentes
**Justificación:**
El componente personalizado no es estático; gestiona sus propios eventos internamente. El `BotonEmergencia` encapsula la lógica del `onClick` para realizar la llamada telefónica usando el parámetro `numero` proporcionado, lo que simplifica enormemente el código de la pantalla padre, que no necesita gestionar esa lógica.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/components/BotonEmergencia.kt#L26-L28

### RA3.f Documentación
**Justificación:**
El código actúa como su propia documentación técnica gracias al uso de identificadores semánticos y claros. Las funciones y variables (como `obtenerTodos`, `insertarAcceso`, `CentrosUrgencias`) describen explícitamente su propósito y funcionamiento, facilitando la comprensión del flujo de datos sin necesidad de comentarios excesivos.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/data/local/AccesoDao.kt#L10
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/data/local/AccesoDao.kt#L13
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/data/local/AccesoDao.kt#L16

### RA3.h Integración en la app
**Justificación:**
Los componentes personalizados desarrollados (`BotonEmergencia`, `GraficoBarrasAccesos`) se integran sin fisuras en las pantallas principales de la aplicación. Conviven con componentes estándar de Material Design dentro de los mismos Layouts, demostrando una correcta arquitectura de composición y modularidad.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaEmergencia.kt#L66-L68

---

## 🔹 RA4. Usabilidad y Accesibilidad

### RA4.a Estándares
**Justificación:**
La aplicación respeta rigurosamente los estándares de navegación y estructura de Android. Se utiliza una `TopAppBar` para el título y acciones contextuales, y un `FloatingActionButton` (donde aplica) o botones principales accesibles, asegurando que el usuario se sienta familiarizado con la interfaz desde el primer uso.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L76-L80

### RA4.b Valoración de estándares
**Justificación:**
Se ha valorado la importancia de la semiótica visual, eligiendo iconos estándar de la librería Material Icons (Lupa para búsqueda, Micrófono para voz, Escudo para policía). Esto reduce la curva de aprendizaje, ya que los usuarios reconocen inmediatamente la función de cada botón sin necesidad de leer etiquetas.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L82-L83
https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L86-L87

### RA4.c Menús
**Justificación:**
Se ha implementado un sistema de menús contextuales en la barra superior (`actions` en `TopAppBar`). No es un menú convencional, pero dependiendo del rol del usuario (Admin o Usuario), se muestran diferentes opciones de navegación (ir a gráficas o ir a emergencias), optimizando el espacio en pantalla y ofreciendo solo las opciones relevantes.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L80-L84

### RA4.d Distribución de acciones
**Justificación:**
La distribución de los elementos interactivos sigue una jerarquía visual clara basada en la importancia. El botón SOS en la pantalla de emergencia es central y masivo, mientras que en la pantalla principal, la barra de búsqueda está arriba y los resultados ocupan el cuerpo, facilitando el flujo de lectura y acción natural de arriba hacia abajo.

<img width="206" height="743" alt="image" src="https://github.com/user-attachments/assets/4ef3df7d-f57b-4787-b7db-281b22d8c7cc" />

### RA4.e Distribución de controles
**Justificación:**
Se ha cuidado el espaciado entre controles interactivos utilizando componentes `Spacer` y modificadores de `padding`. Esto es vital para evitar el "fat finger error" (pulsaciones accidentales en botones contiguos), mejorando la accesibilidad motora y la limpieza visual de la interfaz.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L124

<img width="206" height="743" alt="image" src="https://github.com/user-attachments/assets/693f76ac-78a4-464f-9103-82e03c6bfbe3" />


### RA4.f Elección de controles
**Justificación:**
Se han seleccionado los controles más adecuados para cada tipo de dato. Se utiliza `OutlinedTextField` para los formularios de login y búsqueda porque delimitan claramente el área de escritura, y `FilterChip` en la administración para alternar filtros rápidamente, lo cual es más eficiente que un menú desplegable.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaLogin.kt#L67-L73

### RA4.g Diseño visual
**Justificación:**
El diseño visual utiliza una paleta de colores intencional. El color rojo (`Color.Red`) se utiliza consistentemente para indicar acciones de emergencia o errores críticos, mientras que el azul y el verde se usan para servicios auxiliares. Este código de colores ayuda al usuario a identificar rápidamente la naturaleza de cada función.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaLogin.kt#L106

### RA4.h Claridad de mensajes
**Justificación:**
El sistema proporciona retroalimentación clara y constante. Si ocurre un error (login fallido, sin GPS), se muestra un mensaje de texto en color rojo o un Toast informativo. Esto asegura que el usuario nunca se quede con la duda de si la aplicación ha respondido a su interacción.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaLogin.kt#L78-L80

### RA4.i Pruebas usabilidad
**Justificación:**
Se han desarrollado pruebas de interfaz de usuario (UI Tests) utilizando `ComposeTestRule`. Estas pruebas verifican automáticamente que los elementos clave, como los campos de texto y los botones de acción ("ENTRAR"), son visibles y renderizan correctamente los textos esperados, garantizando la usabilidad básica.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/androidTest/java/com/alberti/relief/PantallaLoginTest.kt#L33

### RA4.j Evaluación en dispositivos
**Justificación:**
El proyecto está configurado para soportar un amplio rango de dispositivos, desde API 24 hasta la 36. El uso de diseños fluidos (`fillMaxWidth`, `weight`) asegura que la interfaz se adapte correctamente a diferentes tamaños de pantalla y densidades de píxeles sin romperse.

---

## 🔹 RA5. Informes y Gráficos (FFOE)

### RA5.a Estructura del informe
**Justificación:**
La aplicación genera informes estructurados programáticamente utilizando la clase `StringBuilder`. Se define una cabecera clara, una sección de resumen estadístico y un cuerpo detallado, creando un documento de texto legible y profesional listo para ser exportado o compartido.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaAdmin.kt#L147-L149

### RA5.b Genera informes a partir de datos
**Justificación:**
Los informes no contienen datos estáticos o "dummy". La aplicación consulta la base de datos local Room en tiempo real para extraer el historial de accesos (`AccesoEntity`), iterando sobre cada registro para construir el cuerpo del informe con datos veraces y actualizados.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaAdmin.kt#L157-L161

### RA5.c Establece filtros
**Justificación:**
El sistema de informes incluye capacidades de filtrado dinámico. A través del objeto de acceso a datos (`AccesoDao`), se ejecutan consultas SQL con cláusulas `WHERE` que permiten al administrador visualizar y reportar únicamente los registros de un rol específico ("ADMIN" o "USUARIO").

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/data/local/AccesoDao.kt#L15-L16

### RA5.d Incluye valores calculados
**Justificación:**
El informe no solo lista datos, sino que procesa la información para ofrecer valor añadido. Se realizan cálculos de agregación en memoria, contando el número total de administradores y usuarios para presentar un resumen estadístico cuantitativo al inicio del informe.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaAdmin.kt#L144-L145

### RA5.e Incluye gráficos
**Justificación:**
Se ha implementado un sistema de visualización de datos mediante un gráfico de barras personalizado. Utilizando primitivas gráficas de Compose (`Box`), se dibuja visualmente la proporción entre administradores y usuarios, ajustando la altura de las barras matemáticamente en función de los valores calculados.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaAdmin.kt#L243-L248

### RA5.g Modificación del código del informe
**Justificación:**
La lógica de generación del informe textual está centralizada en una función específica (`compartirInforme`). Esto permite realizar modificaciones en el formato, añadir nuevos campos o cambiar el estilo del reporte editando un único punto del código, sin afectar a la lógica de visualización en pantalla.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaAdmin.kt#L143-L147

### RA5.h App con informes integrados
**Justificación:**
El módulo de informes y auditoría no es una herramienta externa, sino que está totalmente integrado en la aplicación. La `PantallaAdmin` forma parte del grafo de navegación principal y es accesible directamente desde la interfaz de usuario para aquellos con permisos adecuados.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/Navigation/AppNavigation.kt#L102-L104

---

## 🔹 RA6. Documentación y Ayudas

### RA6.a Identifica sistemas de ayuda
**Justificación:**
Se han identificado e implementado mecanismos de ayuda integrados en los componentes de UI. Se utilizan placeholders en los campos de búsqueda que instruyen al usuario sobre qué debe escribir ("Busca una ciudad..."), proporcionando orientación antes de la interacción.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaPrincipal.kt#L99-L104

### RA6.b Genera ayudas en formatos habituales
**Justificación:**
Los formularios siguen los patrones habituales de ayuda mediante el uso de etiquetas (`labels`). Estas etiquetas permanecen visibles (flotando o fijas) incluso cuando el usuario escribe, asegurando que siempre se conozca el propósito del dato que se está introduciendo.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaLogin.kt#L67-L70

### RA6.c Ayudas sensibles al contexto
**Justificación:**
La aplicación proporciona ayuda específica basada en el contexto de la pantalla. En la pantalla de emergencia, se muestra un texto explicativo justo debajo del botón SOS que aclara cómo interactuar con él ("Mantén pulsado..."), resolviendo la duda del usuario en el momento exacto de la acción.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/screen/PantallaEmergencia.kt#L62

### RA6.d Documenta estructura persistente
**Justificación:**
La estructura de la información persistente (Base de Datos) está documentada explícitamente en el código a través de las anotaciones de la librería Room. La clase de datos `AccesoEntity` define claramente el esquema de la tabla, los tipos de datos y las claves primarias, sirviendo como referencia técnica. Además la propia función está documentada para que el usuario entienda su funcionamiento.

https://github.com/juanma-ge/Relief/blob/c8db87b4494352b7f30df53578c9eec84930eff9/app/src/main/java/com/alberti/relief/data/local/AccesoEntity.kt#L8-L11
https://github.com/juanma-ge/Relief/blob/9c2df39b241d7631797e268de79760cfcf2f17e5/app/src/main/java/com/alberti/relief/data/local/AccesoDao.kt#L7-L12

### RA6.e Manuales y Tutoriales
**Justificación:**
Para cumplir con la entrega de documentación de usuario, se ha elaborado y referenciado un manual de usuario externo en formato Markdown (`MANUAL_USUARIO.md`). Este documento se incluye en el repositorio y explica el funcionamiento de la app, complementando las ayudas integradas.

---

## 🔹 RA7. Distribución (FFOE)

### RA7.a Empaquetado
**Justificación:**
El proyecto está configurado para generar un artefacto instalable único. En el archivo `build.gradle` se definen el `applicationId` y los códigos de versión, permitiendo al sistema de construcción de Android empaquetar todos los recursos y código compilado en un archivo .apk como el de a conituación.

[![Descargar APK](https://img.shields.io/badge/Android-Descargar%20APK-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://github.com/juanma-ge/Relief/releases/download/v1.0/Relief.apk)

### RA7.b Personalización instalador
**Justificación:**
Se ha personalizado la apariencia de la aplicación para el instalador y el launcher del sistema operativo. En el `AndroidManifest.xml`, se especifican el icono (`android:icon`) y el nombre visible (`android:label`), asegurando que la app sea reconocible y profesional tras la instalación.

### RA7.c Paquete desde entorno
**Justificación:**
La generación del paquete instalable se realiza directamente desde el entorno de desarrollo (IDE) utilizando los plugins de Android Gradle. Esto automatiza el proceso de compilación, linkado de recursos y empaquetado final sin necesidad de herramientas de terceros complejas.

### RA7.d Herramientas externas
**Justificación:**
La aplicación integra dependencias de herramientas externas esenciales para su funcionamiento, como `play-services-location`. Estas librerías se gestionan a través de Gradle, que las descarga e incluye en el paquete final para asegurar que la geolocalización funcione en el dispositivo del usuario.

### RA7.e Firma digital
**Justificación:**
El script de construcción está preparado para soportar el firmado digital de la aplicación en modo release. Aunque aquí se muestra la configuración básica, esta estructura permite inyectar las claves del almacén (keystore) para firmar el APK, garantizando su autenticidad e integridad.

### RA7.f Instalación desatendida
**Justificación:**
Para facilitar el despliegue y funcionamiento sin interrupciones constantes, se declaran explícitamente todos los permisos necesarios (Ubicación, Internet o Audio) en el Manifiesto. Esto permite que el sistema gestione los permisos de forma centralizada, facilitando la administración en entornos controlados.

### RA7.g Desinstalación
**Justificación:**
Se configuran las reglas de copia de seguridad (`backup_rules`) en el Manifiesto. Esto asegura que, en caso de desinstalación y reinstalación (o cambio de dispositivo), los datos de usuario se gestionen correctamente según la política definida, permitiendo una limpieza o restauración adecuada.

### RA7.h Canales distribución
**Justificación:**
El control de versiones está explícitamente definido mediante `versionName` y `versionCode`. Esto es fundamental para gestionar los canales de distribución (alfa, beta, producción), ya que las tiendas de aplicaciones y el sistema operativo utilizan estos valores para detectar y aplicar actualizaciones.

---

## 🔹 RA8. Pruebas Avanzadas (FFOE)

### RA8.a Estrategia de pruebas
**Justificación:**
Se ha definido una estrategia de pruebas integral que abarca diferentes niveles de abstracción. Se incluyen dependencias para pruebas unitarias (`JUnit`) para la lógica pura, y pruebas instrumentadas (`Espresso/ComposeTest`) para validar la interacción de la UI y la base de datos en el dispositivo.

### RA8.b Pruebas de integración
**Justificación:**
Se han implementado pruebas de integración específicas para la capa de persistencia. El test `escribirUsuarioYLeerlo` verifica que el DAO interactúa correctamente con la base de datos Room, confirmando que los datos se escriben en disco y se pueden recuperar, validando la integración del sistema.

https://github.com/juanma-ge/Relief/blob/ddfdb4215867368f09eb6e6c658b13c4e2f2fa41/app/src/androidTest/java/com/alberti/relief/AccesoDaoTest.kt#L40-L42

### RA8.c Pruebas de regresión
**Justificación:**
Las pruebas automatizadas actúan como una red de seguridad contra regresiones. Al verificar aserciones específicas (como que el correo recuperado sea igual al insertado), se garantiza que futuros cambios en el código no rompan la funcionalidad de persistencia ya existente.

### RA8.d Pruebas de volumen/estrés
**Justificación:**
Se ha diseñado una prueba de estrés (`pruebaDeCargaMasiva`) que somete a la base de datos a una carga de trabajo intensa (1000 inserciones consecutivas). Esta prueba mide el tiempo de ejecución para asegurar que la aplicación mantiene su rendimiento y estabilidad incluso bajo condiciones extremas de uso.

https://github.com/juanma-ge/Relief/blob/ddfdb4215867368f09eb6e6c658b13c4e2f2fa41/app/src/androidTest/java/com/alberti/relief/AccesoDaoTest.kt#L52-L60

### RA8.e Pruebas de seguridad
**Justificación:**
Se han creado pruebas unitarias (`LoginValidationTest`) dedicadas a validar la lógica de seguridad. Estas pruebas verifican que el sistema rechaza correctamente contraseñas cortas o correos mal formados, asegurando que las reglas de negocio de seguridad se cumplen antes de permitir el acceso.

https://github.com/juanma-ge/Relief/blob/ddfdb4215867368f09eb6e6c658b13c4e2f2fa41/app/src/test/java/com/alberti/relief/LoginValidationTest.kt#L24-L28

### RA8.f Uso de recursos
**Justificación:**
El código de pruebas demuestra una gestión responsable de los recursos del sistema. Se utiliza la anotación `@After` para cerrar la conexión a la base de datos después de cada prueba, evitando fugas de memoria y asegurando que el entorno de pruebas se mantiene limpio y eficiente.

https://github.com/juanma-ge/Relief/blob/ddfdb4215867368f09eb6e6c658b13c4e2f2fa41/app/src/androidTest/java/com/alberti/relief/AccesoDaoTest.kt#L34-L38

### RA8.g Documentación pruebas
**Justificación:**
Las pruebas están documentadas mediante el uso de nombres de funciones largos y descriptivos (`escribirUsuarioYLeerlo`, `pruebaDeCargaMasiva`). Esto permite que cualquier desarrollador entienda inmediatamente el objetivo de la prueba y el escenario que se está validando sin necesidad de leer el cuerpo del código. Además la propia función está documentada para que el usuario entienda el funcionamiento de la prueba.

https://github.com/juanma-ge/Relief/blob/ddfdb4215867368f09eb6e6c658b13c4e2f2fa41/app/src/androidTest/java/com/alberti/relief/AccesoDaoTest.kt#L53
https://github.com/juanma-ge/Relief/blob/64947e11fdfd29b167a27d43ffad4df31d370332/app/src/androidTest/java/com/alberti/relief/AccesoDaoTest.kt#L19-L35
