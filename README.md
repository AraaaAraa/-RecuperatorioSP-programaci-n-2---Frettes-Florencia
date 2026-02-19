# Sistema de Gestión de Productos de Limpieza (JavaFX + JSON)

## 📝 Descripción
Este proyecto es una aplicación de escritorio diseñada para gestionar el inventario de productos de limpieza, permitiendo diferenciar entre productos **Químicos** y **Ecológicos**. El sistema no solo permite el mantenimiento básico (CRUD), sino que también implementa lógica de negocio para la prevención de vencimientos y la exportación de reportes dinámicos.

Desarrollado como proyecto académico para la cátedra de Programación II (UTN).

## ✨ Características Principales
- **Gestión Integral (CRUD):** Alta, baja, modificación y consulta de productos con validaciones en tiempo real.
- **Arquitectura Robusta:** Aplicación de principios de Programación Orientada a Objetos (Herencia, Polimorfismo y Abstracción) mediante una clase base `ProductosGeneral`.
- **Persistencia de Datos:** Almacenamiento y carga automática de datos en formato **JSON** utilizando la librería GSON.
- **Lógica de Alerta de Vencimiento:** Algoritmo que identifica productos que vencerán en los próximos 60 días.
- **Generación de Reportes:** Función para exportar listados específicos de productos próximos a vencer a archivos JSON externos.
- **Manejo de Excepciones Personalizadas:** Control detallado de errores (productos duplicados, nombres vacíos, etc.) para mejorar la experiencia de usuario.

## 🛠️ Tecnologías Utilizadas
- **Lenguaje:** Java 21.
- **Interfaz Gráfica:** JavaFX (FXML).
- **Gestión de Dependencias:** Ant / NetBeans Project.
- **Librerías Externas:** Google GSON para el procesamiento de JSON.

## 📂 Estructura del Proyecto
- `src/Modelos`: Definición de clases de entidad y lógica de herencia.
- `src/Controladores`: Lógica de control de las vistas y gestión de datos (`ProdManager`).
- `src/Vistas`: Archivos FXML para la interfaz de usuario.
- `src/Excepciones`: Definición de errores personalizados del dominio.

## 🚀 Cómo Ejecutarlo
1. Clona el repositorio.
2. Asegúrate de tener instalado el **Java SDK 21** y las librerías de **JavaFX**.
3. Configura las librerías de JavaFX y GSON en tu IDE (NetBeans/IntelliJ).
4. Ejecuta la clase `ProductosLimpieza.java`.
