# 📅 Sistema de Reserva de Recursos

Sistema de escritorio desarrollado en **Java** para la gestión y reserva de recursos dentro de una organización. El sistema permite a los funcionarios reservar recursos como salas, computadoras y proyectores para sus actividades, mientras que los administradores pueden gestionar funcionarios, categorías y recursos.

Proyecto desarrollado para el curso **EIF206 – Programación 3** de la **Universidad Nacional de Costa Rica**.

---

## 🎯 Descripción

El sistema permite administrar las reservas de diferentes recursos disponibles en una organización.

Los funcionarios pueden crear y cancelar reservas, consultar sus actividades y visualizar la disponibilidad de los recursos. Los administradores cuentan además con herramientas para gestionar funcionarios, categorías y recursos.

Una de las principales características del sistema es la posibilidad de utilizar **Inteligencia Artificial** para interpretar una descripción escrita en lenguaje natural y utilizarla para completar automáticamente los datos de una reserva.

---

## ✨ Funcionalidades

### 🔐 Inicio de sesión

* Inicio de sesión mediante ID y contraseña.
* Manejo de roles:

  * 👨‍💼 Administrador
  * 👨‍💻 Funcionario
* Cambio de contraseña.

### 📅 Reservas

* Crear nuevas reservas.
* Seleccionar fecha y horario.
* Seleccionar categorías de recursos.
* Validar disponibilidad.
* Asignar automáticamente el primer recurso disponible de cada categoría.
* Mostrar las categorías que no se encuentran disponibles.
* Modificar una reserva antes de intentar registrarla nuevamente.
* Cancelar reservas futuras.

### 👥 Gestión de funcionarios

Disponible para administradores:

* Registrar funcionarios.
* Consultar funcionarios.
* Modificar información.
* Eliminar funcionarios.
* Buscar por ID o nombre.
* Registrar nombre y teléfono.

### 🗂️ Gestión de categorías

Disponible para administradores:

* Crear categorías.
* Consultar categorías.
* Modificar categorías.
* Eliminar categorías.
* Buscar categorías por descripción.

### 💻 Gestión de recursos

Disponible para administradores:

* Registrar recursos.
* Consultar recursos.
* Modificar recursos.
* Eliminar recursos.
* Filtrar recursos por categoría.
* Asociar recursos con una categoría.

### 🗓️ Calendarización de recursos

Permite visualizar la disponibilidad de los recursos mediante una matriz:

* Filtrado por fecha.
* Selección de categoría.
* Horas del día en las filas.
* Recursos disponibles en las columnas.
* Visualización de reservas y actividades.

### 📋 Calendarización de actividades

Permite visualizar las actividades programadas semanalmente:

* Horas del día.
* Días de la semana.
* Actividades programadas.
* Funcionario responsable.

### 📊 Estadísticas

El sistema genera estadísticas sobre:

* Recursos reservados durante un período.
* Cantidad de recursos utilizados por categoría.
* Actividades calendarizadas.
* Cantidad de actividades por semana.
* Gráficos de barras.

### 🤖 Integración con Inteligencia Artificial

El sistema permite ingresar una descripción de una actividad utilizando lenguaje natural.

La IA analiza la descripción para extraer información como:

* Actividad.
* Fecha.
* Hora de inicio.
* Hora de finalización.
* Recursos requeridos.

Los datos generados pueden ser modificados por el usuario antes de realizar la reserva.

### 📄 Reportes PDF

Las funcionalidades del sistema incluyen generación de reportes en formato **PDF**.

---

## 🏗️ Arquitectura

El proyecto utiliza una **arquitectura por capas** y el patrón **Modelo-Vista-Controlador (MVC)** para la interfaz gráfica, siguiendo los requerimientos establecidos para el proyecto.

```text
┌─────────────────────────────┐
│            VIEW             │
│       Interfaz gráfica      │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│         CONTROLLER          │
│     Lógica de interacción   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│           SERVICE           │
│      Lógica del negocio     │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│            DATA             │
│       Persistencia XML      │
└─────────────────────────────┘
```

---

## 🛠️ Tecnologías utilizadas

| Tecnología                | Uso                                                 |
| ------------------------- | --------------------------------------------------- |
| ☕ Java                    | Lenguaje principal                                  |
| 🖥️ Java Swing            | Interfaz gráfica                                    |
| 🏛️ MVC                   | Arquitectura de la interfaz                         |
| 🧱 Arquitectura por capas | Organización del sistema                            |
| 📄 XML                    | Persistencia de datos                               |
| 📑 PDF                    | Generación de reportes                              |
| 🤖 LLM / IA               | Procesamiento de reservas mediante lenguaje natural |
| 🧪 JUnit Jupiter          | Pruebas automatizadas                               |
| 📦 Maven                  | Gestión y ejecución del proyecto                    |
| 🔀 Git / GitHub           | Control de versiones                                |

El proyecto debe incluir pruebas de **unidad mediante Surefire Plugin** y pruebas de **integración mediante Failsafe Plugin**.

---

## 📁 Estructura del proyecto

```text
src/
├── main/
│   ├── java/
│   │   └── cr.ac.una.reservas/
│   │       ├── model/
│   │       ├── view/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── data/
│   │       └── util/
│   │
│   └── resources/
│       ├── xml/
│       └── ...
│
└── test/
    └── java/
        ├── unit/
        └── integration/
```

> La estructura puede cambiar según la implementación final del proyecto.

---

## 👤 Roles del sistema

### Administrador

Puede gestionar:

* Funcionarios
* Categorías de recursos
* Recursos
* Calendarización
* Estadísticas
* Reservas y demás funcionalidades permitidas.

### Funcionario

Puede:

* Crear reservas.
* Consultar sus reservas.
* Cancelar reservas futuras.
* Consultar disponibilidad.
* Visualizar la calendarización.
* Consultar estadísticas.
* Utilizar la funcionalidad de IA para crear reservas.

---

## 🧪 Pruebas

Se implementarán pruebas automatizadas utilizando **JUnit Jupiter**.

### Pruebas unitarias

Se utilizan para comprobar individualmente el funcionamiento de las diferentes clases y componentes.

### Pruebas de integración

Se utilizan para comprobar la interacción entre diferentes componentes del sistema.

---

## 📸 Capturas de pantalla

### 🔐 Login

![Login](docs/screenshots/login.png)

### 📅 Reservas

![Reservas](docs/screenshots/reservas.png)

### 👥 Funcionarios

![Funcionarios](docs/screenshots/funcionarios.png)

### 💻 Recursos

![Recursos](docs/screenshots/recursos.png)

### 📊 Estadísticas

![Estadísticas](docs/screenshots/estadisticas.png)

> Las imágenes pueden agregarse posteriormente conforme se desarrollen las diferentes interfaces.

---

## 🚀 Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

### 2. Abrir el proyecto

Abrir el proyecto utilizando el IDE correspondiente.

### 3. Ejecutar Maven

```bash
mvn clean install
```

### 4. Ejecutar la aplicación

Ejecutar la clase principal del proyecto.

---

## 🧪 Ejecutar pruebas

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

Para ejecutar el ciclo completo de pruebas:

```bash
mvn verify
```

---

## 👨‍💻 Equipo

**EIF206 – Programación 3**
Universidad Nacional
Facultad de Ciencias Exactas y Naturales
Escuela de Informática

### Integrantes

* 👤 Warner Guevara
* 👤 Javier Acosta
* 👤 Aaron Aguero

---

## 📚 Proyecto académico

Este proyecto fue desarrollado como parte del **Proyecto #1: Sistema de Reserva de Recursos** del curso **EIF206 – Programación 3 (2026-II)**.

El proyecto debe mantenerse actualizado en un repositorio compartido de GitHub durante su desarrollo.

---

## 📌 Estado del proyecto

🚧 **En desarrollo**

* [ ] Login
* [ ] Cambio de contraseña
* [ ] Gestión de reservas
* [ ] Gestión de funcionarios
* [ ] Gestión de categorías
* [ ] Gestión de recursos
* [ ] Calendarización de recursos
* [ ] Calendarización de actividades
* [ ] Estadísticas
* [ ] Integración con IA
* [ ] Reportes PDF
* [ ] Pruebas unitarias
* [ ] Pruebas de integración
