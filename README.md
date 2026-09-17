# 📅 Sistema de Reserva de Recursos

Sistema de escritorio desarrollado en **Java** para la administración, calendarización y reserva de recursos dentro de una organización.

La aplicación permite gestionar funcionarios, categorías y recursos, realizar reservas según disponibilidad, consultar actividades calendarizadas, visualizar estadísticas mediante gráficos y generar reportes en formato PDF. Además, cuenta con persistencia de datos en XML para conservar la información entre ejecuciones.

Proyecto desarrollado para el curso **EIF206 - Programación 3** de la **Universidad Nacional de Costa Rica**.

---

## 🎯 Descripción

El **Sistema de Reserva de Recursos** tiene como objetivo facilitar la administración y utilización de recursos compartidos dentro de una organización.

Los funcionarios pueden crear reservas indicando una actividad, fecha, horario y las categorías de recursos necesarias. El sistema comprueba la disponibilidad y asigna automáticamente el primer recurso disponible de cada categoría seleccionada.

Los administradores disponen de herramientas adicionales para gestionar funcionarios, categorías y recursos, además de consultar la calendarización y las estadísticas generales del sistema.

La aplicación utiliza una interfaz gráfica desarrollada con **Java Swing**, implementada mediante el patrón **Modelo-Vista-Controlador (MVC)** y comunicación mediante `PropertyChange`.

---

# ✨ Funcionalidades

## 🔐 Inicio de sesión

El sistema cuenta con autenticación y control de acceso según el tipo de usuario.

* Inicio de sesión mediante ID y contraseña.
* Validación de credenciales.
* Manejo de roles:

  * Administrador.
  * Funcionario.
* Cambio de contraseña.
* Cierre de sesión.
* Persistencia de los cambios antes de finalizar la aplicación.

---

## 📅 Gestión de reservas

Los funcionarios pueden administrar sus propias reservas.

* Crear nuevas reservas.
* Ingresar el nombre o descripción de una actividad.
* Seleccionar una fecha.
* Seleccionar la hora de inicio y finalización.
* Seleccionar una o varias categorías de recursos.
* Validar los datos antes de enviarlos a la lógica.
* Comprobar disponibilidad según fecha y horario.
* Asignar automáticamente el primer recurso disponible.
* Detectar conflictos de horario.
* Informar cuáles categorías no tienen disponibilidad.
* Consultar reservas existentes.
* Cancelar reservas futuras.

Cada reserva queda asociada con el funcionario que la realizó.

---

## 🤖 Creación de reservas con Inteligencia Artificial

El sistema permite completar automáticamente el formulario de una reserva mediante una frase escrita en lenguaje natural.

Por ejemplo, el funcionario puede escribir:

```text
Necesito una laptop Windows 11 y un proyector para una reunión
el 25 de septiembre de 2026, desde las 9:00 hasta las 11:00.
```

Al presionar el botón **Extraer IA**, el sistema analiza la frase y obtiene los siguientes datos:

* Nombre o descripción de la actividad.
* Fecha de la reserva.
* Hora de inicio.
* Hora de finalización.
* Categorías de recursos solicitadas.

Los datos extraídos se colocan automáticamente en los campos del formulario. Después, el funcionario puede revisarlos, modificarlos o completarlos antes de registrar la reserva.

La Inteligencia Artificial solamente completa el formulario. La reserva no se guarda automáticamente y debe pasar por las mismas validaciones de una reserva creada manualmente.

### Flujo de la Inteligencia Artificial

```text
Frase escrita por el usuario
            ↓
           View
            ↓
        Controller
            ↓
     Service.extractIA()
            ↓
       LangChain4j
            ↓
      GPT-4o mini
            ↓
   ReservaExtraccion
            ↓
       ReservaIA
            ↓
Formulario completado
            ↓
Revisión del funcionario
            ↓
Registro de la reserva
```

### Clases utilizadas

| Clase                     | Responsabilidad                                                                                    |
| ------------------------- | -------------------------------------------------------------------------------------------------- |
| `ReservaExtractorService` | Contiene las instrucciones que debe seguir el modelo de lenguaje para analizar la frase.           |
| `ReservaExtraccion`       | Recibe la respuesta estructurada producida por la Inteligencia Artificial.                         |
| `ReservaIA`               | Almacena los datos convertidos a tipos utilizados por el programa, como `LocalDate` y `LocalTime`. |
| `Service.extractIA()`     | Envía la frase al modelo, procesa la respuesta y valida los datos obtenidos.                       |
| `Controller.extractIA()`  | Busca las categorías extraídas y actualiza el modelo de la pantalla de reservas.                   |
| `View` de reservas        | Obtiene la frase del usuario y muestra los datos generados en el formulario.                       |

### Reglas de extracción

El modelo de lenguaje debe cumplir las siguientes reglas:

* Las fechas deben utilizar el formato `AAAA-MM-DD`.
* Las horas deben utilizar el formato de 24 horas `HH:mm`.
* Solo puede seleccionar categorías existentes en el sistema.
* No puede inventar categorías de recursos.
* Si un dato no está presente y no puede determinarse, debe devolverlo como nulo.
* Puede interpretar expresiones como “mañana”, “el viernes” o “la próxima semana” utilizando la fecha actual como referencia.

### Tecnologías utilizadas para la IA

La funcionalidad utiliza:

* LangChain4j 0.36.0.
* GPT-4o mini.
* Respuestas estructuradas.
* Conversión de texto a objetos Java.
* `LocalDate` y `LocalTime` para fechas y horarios.

Actualmente, el modelo está configurado mediante el endpoint de demostración incluido en el proyecto.

> La función de Inteligencia Artificial requiere conexión a Internet. Si el servicio no está disponible, la reserva todavía puede completarse manualmente.

---

## 👥 Gestión de funcionarios

Esta funcionalidad se encuentra disponible para administradores.

Permite:

* Registrar funcionarios.
* Consultar funcionarios registrados.
* Modificar información.
* Eliminar funcionarios.
* Buscar funcionarios por ID o nombre.
* Registrar:

  * ID.
  * Nombre.
  * Teléfono.
  * Usuario y contraseña.

Cuando se registra un nuevo funcionario, el sistema genera un usuario asociado cuya contraseña inicial es igual al ID del funcionario.

La información se presenta mediante componentes `JTable` y clases `TableModel`.

---

## 🗂️ Gestión de categorías

Esta funcionalidad se encuentra disponible para administradores.

Permite:

* Crear categorías.
* Consultar categorías.
* Modificar categorías.
* Eliminar categorías.
* Buscar categorías por descripción.
* Generar automáticamente sus identificadores.

Ejemplos de categorías:

```text
Laptop Windows 11
Sala para 10 personas
Proyector
Laboratorio
```

Una categoría puede contener varios recursos.

---

## 💻 Gestión de recursos

Esta funcionalidad se encuentra disponible para administradores.

Permite:

* Registrar recursos.
* Consultar recursos.
* Modificar recursos.
* Eliminar recursos.
* Buscar recursos.
* Filtrar recursos por categoría.
* Asociar cada recurso con una categoría.

Una categoría puede contener varios recursos, lo cual permite realizar reservas simultáneas siempre que existan unidades disponibles.

Ejemplo:

```text
Categoría: Proyectores

├── Proyector Epson
├── Proyector Sony
└── Proyector BenQ
```

---

# 🗓️ Calendarización

## Calendarización de recursos

El sistema permite consultar la ocupación de los recursos según una fecha y una categoría determinada.

La información presenta:

* Recursos pertenecientes a la categoría seleccionada.
* Horas de inicio y finalización.
* Actividades registradas.
* Funcionarios responsables.
* Filtrado por fecha.
* Filtrado por categoría.

Esto permite identificar cuáles recursos se encuentran ocupados y cuáles están disponibles.

---

## 📋 Calendarización de actividades

El sistema también permite consultar la planificación semanal de las actividades.

La calendarización muestra:

* Días de la semana.
* Horas del día.
* Actividades programadas.
* Reservas asociadas.
* Funcionario responsable.

Esto facilita la visualización de las actividades registradas durante una semana.

---

# 📊 Estadísticas

El sistema incorpora un módulo de estadísticas que permite consultar información dentro de un periodo determinado.

## Recursos reservados por categoría

Permite visualizar cuántos recursos fueron utilizados en cada categoría.

La información se presenta mediante:

* Tabla de resultados.
* Gráfico de barras.
* Selección de fecha inicial.
* Selección de fecha final.

## Actividades por semana

Permite visualizar la cantidad de actividades programadas durante las semanas comprendidas en el periodo seleccionado.

La información se presenta mediante:

* Tabla de resultados.
* Gráfico de barras.
* Cantidad de actividades por semana.

Los gráficos se actualizan cuando se carga un nuevo periodo.

Si no existen datos para el rango seleccionado, la interfaz informa que no hay información disponible.

---

# 📄 Generación de reportes PDF

El sistema permite exportar la información de sus principales tablas a archivos PDF.

Los reportes se encuentran disponibles en:

* Categorías.
* Funcionarios.
* Recursos.
* Reservas.
* Calendarización.
* Actividades.
* Estadísticas.

Al seleccionar la opción **Imprimir**, el sistema abre un selector que permite elegir la ubicación y el nombre del archivo.

Si la tabla se encuentra filtrada, el PDF contiene únicamente la información mostrada.

```text
Buscar → Mostrar resultados → Imprimir
                              ↓
                         reporte.pdf
```

Después de generar el archivo, el sistema intenta abrirlo automáticamente.

---

# 💾 Persistencia de datos

El sistema utiliza el archivo `data.xml` para conservar la información.

Los cambios almacenados incluyen:

* Usuarios.
* Funcionarios.
* Categorías.
* Recursos.
* Reservas.
* Cancelaciones.
* Cambios de contraseña.

La persistencia sigue el siguiente flujo:

```text
Usuario realiza una operación
            ↓
           View
            ↓
        Controller
            ↓
         Service
            ↓
           Data
            ↓
         data.xml
```

Al iniciar nuevamente el programa:

```text
data.xml
    ↓
XmlPersister
    ↓
Service
    ↓
Data
    ↓
Aplicación
```

De esta manera, la información permanece disponible después de cerrar y volver a abrir el sistema.

---

# 🏗️ Arquitectura

El proyecto utiliza una arquitectura por capas junto con el patrón **Modelo-Vista-Controlador**.

```text
┌──────────────────────────────────┐
│               VIEW               │
│         Interfaz Java Swing      │
│                                  │
│ take() · validate() · listeners  │
└────────────────┬─────────────────┘
                 │
                 ▼
┌──────────────────────────────────┐
│            CONTROLLER            │
│      Coordina las operaciones    │
└────────────────┬─────────────────┘
                 │
                 ▼
┌──────────────────────────────────┐
│             SERVICE              │
│        Lógica del negocio        │
└────────────────┬─────────────────┘
                 │
                 ▼
┌──────────────────────────────────┐
│              DATA                │
│        Información del sistema   │
└────────────────┬─────────────────┘
                 │
                 ▼
┌──────────────────────────────────┐
│             XML                  │
│       Persistencia data.xml      │
└──────────────────────────────────┘
```

## Capa de presentación

Contiene las vistas, modelos, controladores y modelos de tabla de cada módulo.

La vista se encarga de:

* Mostrar la interfaz gráfica.
* Leer los datos introducidos.
* Realizar validaciones iniciales.
* Escuchar las acciones del usuario.
* Actualizar sus componentes cuando cambia el modelo.

## Capa lógica

Contiene las entidades y la clase `Service`.

Se encarga de:

* Aplicar las reglas del negocio.
* Validar los datos.
* Comprobar la disponibilidad de recursos.
* Asignar recursos.
* Gestionar usuarios.
* Calcular estadísticas.
* Procesar la extracción mediante IA.

## Capa de datos

Contiene:

* `Data`.
* `XmlPersister`.
* Adaptadores para `LocalDate`.
* Adaptadores para `LocalTime`.

Esta capa carga y guarda la información mediante JAXB.

---

# 🔄 Implementación MVC

## `TableModel`

Los `TableModel` transforman las colecciones de objetos en filas y columnas que pueden ser utilizadas por los componentes `JTable`.

```text
List<Funcionario>
        ↓
   TableModel
        ↓
     JTable
```

Se utilizan modelos de tabla para representar:

* Funcionarios.
* Categorías.
* Recursos.
* Reservas.
* Actividades.
* Calendarización.

---

## `PropertyChange`

Se utilizan `PropertyChangeSupport` y `PropertyChangeListener` para comunicar los cambios entre los modelos y las vistas.

```text
Model cambia
     ↓
PropertyChange
     ↓
View recibe el cambio
     ↓
JTable y componentes se actualizan
```

Esto evita que el modelo dependa directamente de los componentes gráficos.

---

## `take()`

Los métodos `take()` obtienen la información ingresada por el usuario desde los componentes gráficos.

```text
JTextField
JComboBox
JList
JSpinner
   ↓
 take()
   ↓
Objeto del dominio
```

---

## `validate()`

Antes de enviar la información al controlador, las vistas realizan validaciones mediante métodos `validate()`.

Estas validaciones permiten detectar:

* Campos vacíos.
* Fechas inválidas.
* Horarios incorrectos.
* Categorías no seleccionadas.
* Información incompleta.

Esto evita enviar datos inválidos a la lógica del negocio.

---

## Listeners

Los componentes Swing utilizan listeners para conectar las acciones del usuario con las funciones correspondientes.

Entre ellos:

* Guardar.
* Buscar.
* Modificar.
* Eliminar.
* Limpiar.
* Reservar.
* Cancelar.
* Cargar información.
* Extraer información mediante IA.
* Imprimir PDF.
* Seleccionar filas de las tablas.

El flujo general de una operación es:

```text
Usuario
   ↓
Listener
   ↓
take()
   ↓
validate()
   ↓
Controller
   ↓
Service
   ↓
Model
   ↓
PropertyChange
   ↓
View
```

---

# 🛠️ Tecnologías utilizadas

| Tecnología             | Uso                                              |
| ---------------------- | ------------------------------------------------ |
| Java                   | Lenguaje principal                               |
| Java Swing             | Desarrollo de la interfaz gráfica                |
| MVC                    | Organización de la interfaz                      |
| PropertyChange         | Comunicación entre Model y View                  |
| TableModel             | Representación de objetos en JTable              |
| Arquitectura por capas | Separación de responsabilidades                  |
| JAXB                   | Conversión entre objetos Java y XML              |
| XML                    | Persistencia de información                      |
| iText 7                | Generación de reportes PDF                       |
| JFreeChart             | Generación de gráficos                           |
| JUnit Jupiter          | Pruebas automatizadas                            |
| Maven                  | Gestión de dependencias                          |
| LangChain4j            | Integración con el modelo de lenguaje            |
| GPT-4o mini            | Extracción de información desde lenguaje natural |
| Git y GitHub           | Control de versiones                             |

---

# 📁 Estructura general del proyecto

```text
SistemaReservas/
├── pom.xml
├── data.xml
└── src/
    ├── main/
    │   └── java/reservas/
    │       ├── Application.java
    │       ├── data/
    │       │   ├── Data.java
    │       │   ├── XmlPersister.java
    │       │   ├── LocalDateAdapter.java
    │       │   └── LocalTimeAdapter.java
    │       ├── logic/
    │       │   ├── Categoria.java
    │       │   ├── Funcionario.java
    │       │   ├── Recurso.java
    │       │   ├── Reserva.java
    │       │   ├── Service.java
    │       │   ├── Usuario.java
    │       │   └── ai/
    │       │       ├── ReservaExtraccion.java
    │       │       ├── ReservaExtractorService.java
    │       │       └── ReservaIA.java
    │       ├── presentation/
    │       │   ├── login/
    │       │   ├── cambioclave/
    │       │   ├── funcionarios/
    │       │   ├── categorias/
    │       │   ├── recursos/
    │       │   ├── reservas/
    │       │   ├── calendarizacion/
    │       │   ├── actividades/
    │       │   └── estadisticas/
    │       └── util/
    │           └── PdfReport.java
    └── test/
        └── java/reservas/
```

Los módulos de presentación contienen normalmente:

```text
Controller.java
Model.java
TableModel.java
View.java
View.form
```

---

# 👤 Roles del sistema

## 👨‍💼 Administrador

El administrador puede:

* Gestionar funcionarios.
* Gestionar categorías.
* Gestionar recursos.
* Consultar la calendarización.
* Consultar actividades.
* Visualizar estadísticas.
* Generar reportes PDF.
* Cambiar su contraseña.

## 👨‍💻 Funcionario

El funcionario puede:

* Crear reservas.
* Utilizar la extracción mediante IA.
* Consultar sus reservas.
* Cancelar reservas futuras.
* Consultar la calendarización.
* Consultar las actividades.
* Visualizar estadísticas.
* Generar reportes PDF.
* Cambiar su contraseña.

---

# 🚀 Instalación y ejecución

## Requisitos

Antes de ejecutar el proyecto se necesita:

* Java JDK 22.
* Maven.
* IntelliJ IDEA o cualquier IDE compatible con Maven.
* Conexión a Internet para utilizar la Inteligencia Artificial.
* Git, en caso de clonar el repositorio.

## Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

Ingresar a la carpeta:

```bash
cd SistemaReservas
```

## Abrir en IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar **File → Open**.
3. Seleccionar la carpeta donde se encuentra `pom.xml`.
4. Esperar a que Maven descargue las dependencias.
5. Configurar el proyecto para utilizar Java 22.
6. Ejecutar `reservas.Application`.

## Compilar desde la terminal

```bash
mvn clean compile
```

Para realizar una construcción completa:

```bash
mvn clean install
```

---

# 🔑 Usuarios iniciales

El archivo `data.xml` contiene usuarios de referencia para probar los dos roles.

## Administrador

```text
ID: ADM001
Clave: 001
```

## Funcionario

```text
ID: USR001
Clave: 001
```

Las contraseñas pueden cambiarse desde la aplicación.

Cuando el administrador registra un nuevo funcionario, su contraseña inicial queda igual que su ID.

---

# 🧪 Pruebas

El proyecto utiliza **JUnit Jupiter** para realizar pruebas automatizadas.

## Pruebas unitarias

Comprueban individualmente el comportamiento de clases y métodos.

## Pruebas de integración

Comprueban el funcionamiento conjunto de las diferentes capas del sistema.

Para ejecutar las pruebas:

```bash
mvn test
```

Para ejecutar todas las verificaciones:

```bash
mvn verify
```

---

# 💾 Consideraciones sobre `data.xml`

El archivo `data.xml` contiene la información persistente del sistema.

No debe eliminarse si se desea conservar:

* Usuarios.
* Funcionarios.
* Categorías.
* Recursos.
* Reservas.
* Contraseñas modificadas.
* Cancelaciones.

Debe permanecer en la carpeta principal del proyecto, al mismo nivel que `pom.xml`.

```text
SistemaReservas/
├── data.xml
├── pom.xml
└── src/
```

---

# 📸 Capturas de pantalla

Se recomienda almacenar las capturas del proyecto dentro de:

```text
docs/screenshots/
```

Ejemplo:

```text
docs/
└── screenshots/
    ├── login.png
    ├── reservas.png
    ├── inteligencia-artificial.png
    ├── funcionarios.png
    ├── categorias.png
    ├── recursos.png
    ├── calendarizacion.png
    ├── actividades.png
    └── estadisticas.png
```

Las imágenes pueden mostrarse en el README de la siguiente manera:

```markdown
![Inicio de sesión](docs/screenshots/login.png)
![Reservas con IA](docs/screenshots/inteligencia-artificial.png)
![Estadísticas](docs/screenshots/estadisticas.png)
```

---

# 👨‍💻 Equipo de desarrollo

Proyecto desarrollado por:

* Warner Guevara.
* Javier Acosta.
* Aaron Agüero.

Curso:

```text
EIF206 - Programación 3
Universidad Nacional de Costa Rica
Facultad de Ciencias Exactas y Naturales
Escuela de Informática
```

---

# 📚 Contexto académico

Este proyecto fue desarrollado como parte del **Proyecto #1: Sistema de Reserva de Recursos** del curso **EIF206 - Programación 3 (2026-II)**.

Su desarrollo permite aplicar conceptos como:

* Programación orientada a objetos.
* Arquitectura por capas.
* Patrón Modelo-Vista-Controlador.
* Interfaces gráficas con Java Swing.
* `PropertyChangeSupport`.
* `PropertyChangeListener`.
* `TableModel`.
* Validación de datos.
* Manejo de eventos mediante listeners.
* Persistencia XML.
* Generación de reportes PDF.
* Creación de gráficos.
* Integración con Inteligencia Artificial.
* Pruebas automatizadas.
* Control de versiones con Git y GitHub.

---

# 📌 Estado del proyecto

## ✅ Funcionalidades implementadas

* [x] Inicio de sesión.
* [x] Manejo de roles.
* [x] Cambio de contraseña.
* [x] Gestión de funcionarios.
* [x] Gestión de categorías.
* [x] Gestión de recursos.
* [x] Gestión de reservas.
* [x] Validación de disponibilidad.
* [x] Asignación automática de recursos.
* [x] Cancelación de reservas futuras.
* [x] Calendarización de recursos.
* [x] Calendarización de actividades.
* [x] Estadísticas.
* [x] Gráficos de barras.
* [x] Generación de reportes PDF.
* [x] Modelos `TableModel`.
* [x] Comunicación mediante `PropertyChange`.
* [x] Métodos `take()`.
* [x] Métodos `validate()`.
* [x] Listeners de la interfaz.
* [x] Persistencia mediante XML.
* [x] Guardado automático de cambios.
* [x] Recuperación de información al reiniciar.
* [x] Lectura de frases en lenguaje natural.
* [x] Extracción del nombre de la actividad.
* [x] Extracción de fecha.
* [x] Extracción de hora inicial y final.
* [x] Extracción de categorías.
* [x] Validación de categorías existentes.
* [x] Actualización automática del formulario.
* [x] Edición manual de los datos generados por IA.
* [x] Integración con LangChain4j y GPT-4o mini.

---

# 🎓 Propósito

El sistema constituye una aplicación de escritorio orientada a demostrar la aplicación práctica de:

* Programación orientada a objetos.
* Arquitectura por capas.
* Modelo-Vista-Controlador.
* Interfaces gráficas.
* Persistencia de datos.
* Manejo de eventos.
* Generación de reportes.
* Visualización de estadísticas.
* Integración con Inteligencia Artificial.

---

**Universidad Nacional de Costa Rica - EIF206 Programación 3 - 2026-II**

