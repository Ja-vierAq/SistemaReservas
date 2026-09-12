# 📅 Sistema de Reserva de Recursos

Sistema de escritorio desarrollado en **Java** para la administración, calendarización y reserva de recursos dentro de una organización.

La aplicación permite gestionar funcionarios, categorías y recursos, realizar reservas según disponibilidad, consultar actividades calendarizadas, visualizar estadísticas mediante gráficos y generar reportes en formato PDF. Además, cuenta con persistencia de datos en XML para conservar la información entre ejecuciones.

Proyecto desarrollado para el curso **EIF206 – Programación 3** de la **Universidad Nacional de Costa Rica**.

---

## 🎯 Descripción

El **Sistema de Reserva de Recursos** tiene como objetivo facilitar la administración y utilización de recursos compartidos dentro de una organización.

Los funcionarios pueden crear reservas indicando una actividad, fecha, horario y las categorías de recursos necesarias. El sistema se encarga de comprobar la disponibilidad y asignar automáticamente un recurso disponible de cada categoría seleccionada.

Los administradores disponen de herramientas adicionales para gestionar funcionarios, categorías y recursos, además de consultar la calendarización y las estadísticas generales del sistema.

La aplicación utiliza una interfaz gráfica desarrollada con **Java Swing**, implementada mediante el patrón **Modelo-Vista-Controlador (MVC)** y comunicación mediante `PropertyChange`.

---

# ✨ Funcionalidades

## 🔐 Inicio de sesión

El sistema cuenta con autenticación y control de acceso según el tipo de usuario.

* Inicio de sesión mediante ID y contraseña.
* Validación de credenciales.
* Manejo de roles:

  * 👨‍💼 Administrador.
  * 👨‍💻 Funcionario.
* Cambio de contraseña.
* Cierre de sesión.
* Persistencia de los cambios realizados antes de finalizar la aplicación.

---

## 📅 Gestión de reservas

Los funcionarios pueden administrar sus propias reservas.

* Crear nuevas reservas.
* Ingresar el nombre o descripción de una actividad.
* Seleccionar fecha.
* Seleccionar hora de inicio y finalización.
* Seleccionar una o varias categorías de recursos.
* Validar los datos antes de enviarlos a la lógica del sistema.
* Comprobar disponibilidad según fecha y horario.
* Asignar automáticamente el primer recurso disponible de cada categoría.
* Detectar conflictos de horario.
* Informar cuando una categoría no posee disponibilidad.
* Consultar reservas existentes.
* Cancelar reservas futuras.

Cada reserva queda asociada al funcionario que la realizó.

---

## 👥 Gestión de funcionarios

Funcionalidad disponible para administradores.

Permite:

* Registrar funcionarios.
* Consultar funcionarios registrados.
* Modificar información.
* Eliminar funcionarios.
* Buscar funcionarios.
* Registrar información como:

  * ID.
  * Nombre.
  * Teléfono.
  * Contraseña.

La información se presenta utilizando `JTable` y `TableModel`.

---

## 🗂️ Gestión de categorías

Funcionalidad disponible para administradores.

Permite:

* Crear categorías.
* Consultar categorías.
* Modificar categorías.
* Eliminar categorías.
* Buscar categorías por descripción.

### 📦 Espacio inicial automático

Cuando se crea una nueva categoría, el sistema genera automáticamente un **recurso o espacio inicial asociado a ella**.

Esto permite que una categoría recién creada pueda ser utilizada inmediatamente al realizar una reserva.

Por ejemplo:

```text
Categoría:
Laboratorio

        ↓

Recurso generado automáticamente:
Espacio inicial - Laboratorio
```

Si una categoría existente no posee ningún recurso asociado, el sistema también puede generar su espacio inicial al cargar los datos.

---

## 💻 Gestión de recursos

Funcionalidad disponible para administradores.

Permite:

* Registrar recursos.
* Consultar recursos.
* Modificar recursos.
* Eliminar recursos.
* Buscar recursos.
* Filtrar recursos por categoría.
* Asociar cada recurso con una categoría.

Una categoría puede contener múltiples recursos, permitiendo realizar varias reservas simultáneas siempre que existan recursos disponibles.

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

El sistema permite consultar la ocupación de los recursos según una fecha y categoría determinada.

La información se presenta mediante una estructura similar a una matriz:

* Horas del día en las filas.
* Recursos en las columnas.
* Reservas existentes en las celdas.
* Filtrado por fecha.
* Filtrado por categoría.

Esto permite identificar visualmente cuáles recursos se encuentran ocupados y cuáles están disponibles.

---

## 📋 Calendarización de actividades

También se puede consultar la planificación semanal de actividades.

La calendarización muestra:

* Días de la semana.
* Horas del día.
* Actividades programadas.
* Reservas asociadas.
* Funcionario responsable.

Esto facilita visualizar la distribución semanal de las actividades registradas en el sistema.

---

# 📊 Estadísticas

El sistema incorpora un módulo de estadísticas que permite consultar información dentro de un período determinado.

Incluye dos secciones principales:

### Recursos reservados por categoría

Permite visualizar cuántos recursos han sido utilizados para cada categoría.

La información se presenta mediante:

* Tabla de resultados.
* Gráfico de barras.

### Actividades por semana

Permite visualizar la cantidad de actividades realizadas durante las diferentes semanas del período seleccionado.

La información se presenta mediante:

* Tabla de resultados.
* Gráfico de barras.

Los gráficos se actualizan automáticamente cuando se carga un nuevo período.

Si no existen datos para el rango seleccionado, la interfaz informa que no hay información disponible.

---

# 📄 Generación de reportes PDF

El sistema permite exportar información de las principales tablas a archivos **PDF**.

La generación de reportes está disponible en:

* Categorías.
* Funcionarios.
* Recursos.
* Reservas.
* Calendarización.
* Actividades.

Al seleccionar la opción **Imprimir**, el sistema abre un selector que permite elegir la ubicación y el nombre del archivo.

Si la tabla se encuentra filtrada, el PDF contiene únicamente la información mostrada actualmente.

Ejemplo:

```text
Buscar → Mostrar resultados → Imprimir
                              ↓
                        reporte.pdf
```

Los reportes PDF son generados directamente desde la aplicación.

---

# 💾 Persistencia de datos

El sistema utiliza un archivo **XML** para conservar la información.

Los cambios importantes son almacenados automáticamente, incluyendo:

* Funcionarios.
* Categorías.
* Recursos.
* Reservas.
* Cancelaciones.
* Cambios de contraseña.

La persistencia sigue aproximadamente el siguiente flujo:

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

Además, el sistema vuelve a guardar la información cuando se cierra la sesión o la aplicación.

Al iniciar nuevamente el programa:

```text
data.xml
    ↓
Service
    ↓
Data
    ↓
Aplicación
```

De esta manera, la información creada durante una ejecución permanece disponible al abrir nuevamente el sistema.

---

# 🏗️ Arquitectura

El proyecto utiliza una **arquitectura por capas** junto con el patrón **Modelo-Vista-Controlador (MVC)**.

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

---

# 🔄 Implementación MVC

La interfaz utiliza diferentes mecanismos para mantener separadas las responsabilidades.

## `TableModel`

Los `TableModel` transforman las colecciones de objetos del sistema en filas y columnas que pueden ser utilizadas por los componentes `JTable`.

```text
List<Funcionario>
        ↓
   TableModel
        ↓
     JTable
```

Se utilizan modelos de tabla para representar elementos como:

* Funcionarios.
* Categorías.
* Recursos.
* Reservas.
* Actividades.
* Calendarización.

---

## `PropertyChange`

Se utiliza `PropertyChangeSupport` y `PropertyChangeListener` para comunicar cambios entre los modelos y las vistas.

```text
Model cambia
     ↓
PropertyChange
     ↓
View recibe el cambio
     ↓
JTable / componentes se actualizan
```

Esto evita que el modelo dependa directamente de los componentes gráficos.

---

## `take()`

Los métodos `take()` se encargan de obtener la información ingresada por el usuario desde los componentes de la interfaz.

Por ejemplo:

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

Antes de enviar información al controlador, las vistas realizan validaciones mediante métodos `validate()`.

Estas validaciones permiten detectar problemas como:

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
* Imprimir PDF.
* Seleccionar filas de tablas.

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

| Tecnología                | Uso                                     |
| ------------------------- | --------------------------------------- |
| ☕ Java                    | Lenguaje principal                      |
| 🖥️ Java Swing            | Desarrollo de la interfaz gráfica       |
| 🏛️ MVC                   | Organización de la interfaz             |
| 🔄 PropertyChange         | Comunicación entre Model y View         |
| 📊 TableModel             | Representación de objetos en JTable     |
| 🧱 Arquitectura por capas | Separación de responsabilidades         |
| 📄 XML                    | Persistencia de información             |
| 📑 PDF                    | Generación de reportes                  |
| 📈 Java AWT / Swing       | Generación y representación de gráficos |
| 🧪 JUnit Jupiter          | Pruebas automatizadas                   |
| 📦 Maven                  | Gestión de dependencias y construcción  |
| 🔀 Git / GitHub           | Control de versiones                    |

---

# 📁 Estructura general del proyecto

La aplicación se encuentra organizada principalmente por módulos y responsabilidades.

```text
SistemaReservas/
│
├── pom.xml
├── data.xml
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── cr/ac/una/reservas/
    │   │
    │   │       ├── categorias/
    │   │       │   ├── Controller.java
    │   │       │   ├── Model.java
    │   │       │   ├── TableModel.java
    │   │       │   └── View.java
    │   │       │
    │   │       ├── funcionarios/
    │   │       │   ├── Controller.java
    │   │       │   ├── Model.java
    │   │       │   ├── TableModel.java
    │   │       │   └── View.java
    │   │       │
    │   │       ├── recursos/
    │   │       │   ├── Controller.java
    │   │       │   ├── Model.java
    │   │       │   ├── TableModel.java
    │   │       │   └── View.java
    │   │       │
    │   │       ├── reservas/
    │   │       │   ├── Controller.java
    │   │       │   ├── Model.java
    │   │       │   ├── TableModel.java
    │   │       │   └── View.java
    │   │       │
    │   │       ├── calendarizacion/
    │   │       ├── actividades/
    │   │       ├── estadisticas/
    │   │       ├── login/
    │   │       ├── cambioClave/
    │   │       ├── service/
    │   │       ├── data/
    │   │       └── util/
    │   │
    │   └── resources/
    │
    └── test/
        └── java/
```

> La estructura exacta puede variar ligeramente dependiendo de la versión final del repositorio.

---

# 👤 Roles del sistema

## 👨‍💼 Administrador

El administrador posee acceso a las funciones de mantenimiento y administración del sistema.

Puede gestionar:

* Funcionarios.
* Categorías.
* Recursos.
* Reservas.
* Calendarización.
* Actividades.
* Estadísticas.
* Reportes PDF.
* Cambio de contraseña.

---

## 👨‍💻 Funcionario

El funcionario utiliza principalmente las funcionalidades relacionadas con las reservas.

Puede:

* Crear reservas.
* Consultar sus reservas.
* Cancelar reservas futuras.
* Consultar disponibilidad.
* Visualizar calendarizaciones.
* Consultar estadísticas permitidas.
* Cambiar su contraseña.

---

# 🧪 Pruebas

El proyecto utiliza **JUnit Jupiter** para las pruebas automatizadas.

Las pruebas pueden dividirse en:

### Pruebas unitarias

Comprueban individualmente el comportamiento de clases y métodos del sistema.

### Pruebas de integración

Comprueban el funcionamiento conjunto de diferentes componentes y capas.

Maven permite ejecutar las pruebas durante el proceso de construcción del proyecto.

---

# 🚀 Instalación y ejecución

## Requisitos

Antes de ejecutar el proyecto se recomienda contar con:

* Java JDK compatible con el proyecto.
* Maven.
* IntelliJ IDEA o cualquier IDE compatible con proyectos Maven.
* Git, en caso de clonar el repositorio.

---

## 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

Luego ingresar al directorio:

```bash
cd SistemaReservas
```

---

## 2. Abrir el proyecto

Abrir la carpeta del proyecto desde **IntelliJ IDEA**.

Al tratarse de un proyecto Maven, IntelliJ puede detectar automáticamente el archivo:

```text
pom.xml
```

y descargar las dependencias necesarias.

---

## 3. Compilar el proyecto

Desde una terminal:

```bash
mvn clean compile
```

También puede utilizarse:

```bash
mvn clean install
```

---

## 4. Ejecutar la aplicación

Ejecutar desde IntelliJ la clase principal (`main`) correspondiente a la aplicación.

El archivo:

```text
data.xml
```

debe permanecer disponible para que el sistema pueda cargar y almacenar la información persistente.

---

# 🧪 Ejecución de pruebas

Para ejecutar las pruebas:

```bash
mvn test
```

Para ejecutar el ciclo completo de validación:

```bash
mvn verify
```

---

# 💾 Consideraciones sobre `data.xml`

El archivo `data.xml` contiene la información persistente utilizada por el sistema.

Por esta razón, no debe eliminarse si se desea conservar:

* Usuarios.
* Categorías.
* Recursos.
* Reservas.
* Contraseñas modificadas.
* Información registrada durante ejecuciones anteriores.

El sistema actualiza este archivo automáticamente cuando se realizan operaciones que modifican los datos.

---

# 📸 Capturas de pantalla

Se recomienda almacenar las capturas del proyecto dentro de:

```text
docs/screenshots/
```

Por ejemplo:

```text
docs/
└── screenshots/
    ├── login.png
    ├── reservas.png
    ├── funcionarios.png
    ├── categorias.png
    ├── recursos.png
    ├── calendarizacion.png
    └── estadisticas.png
```

Posteriormente pueden mostrarse en este README:

### 🔐 Inicio de sesión

![Login](docs/screenshots/login.png)

### 📅 Reservas

![Reservas](docs/screenshots/reservas.png)

### 👥 Funcionarios

![Funcionarios](docs/screenshots/funcionarios.png)

### 🗂️ Categorías

![Categorías](docs/screenshots/categorias.png)

### 💻 Recursos

![Recursos](docs/screenshots/recursos.png)

### 📊 Estadísticas

![Estadísticas](docs/screenshots/estadisticas.png)

---

# 👨‍💻 Equipo de desarrollo

**EIF206 – Programación 3**
**Universidad Nacional de Costa Rica**
Facultad de Ciencias Exactas y Naturales
Escuela de Informática

### Integrantes

* 👤 Warner Guevara
* 👤 Javier Acosta
* 👤 Aaron Aguero

---

# 📚 Contexto académico

Este proyecto fue desarrollado como parte del **Proyecto #1: Sistema de Reserva de Recursos** del curso **EIF206 – Programación 3 (2026-II)**.

Su desarrollo busca aplicar conceptos estudiados durante el curso, entre ellos:

* Programación orientada a objetos.
* Arquitectura por capas.
* Patrón MVC.
* Interfaces gráficas con Java Swing.
* `PropertyChangeSupport`.
* `PropertyChangeListener`.
* `TableModel`.
* Validación de datos.
* Manejo de eventos mediante listeners.
* Persistencia de información.
* Generación de reportes.
* Pruebas automatizadas.
* Control de versiones con Git.

---

# 📌 Estado del proyecto

## ✅ Funcionalidades implementadas

* [x] Inicio de sesión.
* [x] Manejo de roles.
* [x] Cambio de contraseña.
* [x] Gestión de funcionarios.
* [x] Gestión de categorías.
* [x] Creación automática de espacio inicial para nuevas categorías.
* [x] Gestión de recursos.
* [x] Gestión de reservas.
* [x] Validación de disponibilidad.
* [x] Asignación automática de recursos.
* [x] Cancelación de reservas.
* [x] Calendarización de recursos.
* [x] Calendarización de actividades.
* [x] Estadísticas.
* [x] Gráficos de barras.
* [x] Generación de reportes PDF.
* [x] `TableModel`.
* [x] `PropertyChange`.
* [x] Métodos `take()`.
* [x] Métodos `validate()`.
* [x] Listeners de la interfaz.
* [x] Persistencia mediante XML.
* [x] Guardado automático de cambios.
* [x] Recuperación de información al reiniciar la aplicación.

---

## 🎓 Propósito

El sistema constituye una aplicación de escritorio completa orientada a demostrar la aplicación práctica de **Programación Orientada a Objetos, MVC, arquitectura por capas, interfaces gráficas, persistencia y manejo de eventos en Java**.

---

**Universidad Nacional de Costa Rica — EIF206 Programación 3 — 2026-II**
