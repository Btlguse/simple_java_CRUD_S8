# Manual de Operaciones
## Sistema de Gestión de Agencia de Viajes (Travel Agency Management System)

**Proyecto:** simple_java_CRUD_S8  
**Versión:** 1.0-SNAPSHOT  
**Última actualización:** Febrero 2026  
**Organización:** Universidad de Guatemala - Grupo 8

---

## 📋 Tabla de Contenidos

1. [Descripción General](#1-descripción-general)
2. [Arquitectura del Sistema](#2-arquitectura-del-sistema)
3. [Requisitos del Sistema](#3-requisitos-del-sistema)
4. [Instalación y Configuración](#4-instalación-y-configuración)
5. [Gestión de Base de Datos](#5-gestión-de-base-de-datos)
6. [Operación de la Aplicación](#6-operación-de-la-aplicación)
7. [Gestión Maven](#7-gestión-maven)
8. [Pruebas y Calidad de Código](#9-pruebas-y-calidad-de-código)
9. [Mantenimiento y Solución de Problemas](#10-mantenimiento-y-solución-de-problemas)
10. [Glosario Técnico](#11-glosario-técnico)

---

## 1. Descripción General

### 1.1 Propósito del Sistema

El Sistema de Gestión de Agencia de Viajes es una aplicación de escritorio desarrollada en Java que permite administrar operaciones básicas de una agencia de viajes, incluyendo:

- **Gestión de Clientes:** Registro, consulta, actualización y eliminación de clientes
- **Gestión de Reservas:** Control de reservas de viajes con información de destinos, fechas y precios
- **Gestión de Facturas:** Emisión y control de facturas asociadas a las reservas

### 1.2 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java (JDK) | 17 | Lenguaje de programación principal |
| Maven | 3.9+ | Gestión de dependencias y construcción |
| MySQL | 8.0+ | Sistema de gestión de base de datos |
| Swing/SwingX | 1.6.1 | Interfaz gráfica de usuario |
| JUnit Jupiter | 5.9.2 | Framework de pruebas unitarias |
| JaCoCo | 0.8.11 | Cobertura de código |
| Jenkins | 2.x | Integración y despliegue continuo |

### 1.3 Características Principales

✅ Interfaz gráfica amigable basada en Swing  
✅ Arquitectura MVC (Modelo-Vista-Controlador)  
✅ Persistencia de datos en MySQL  
✅ Validación de datos de entrada  
✅ Pruebas unitarias automatizadas  
✅ Pipeline CI/CD automatizado  
✅ Reportes de cobertura de código  

---

## 2. Arquitectura del Sistema

### 2.1 Patrón de Diseño: MVC

```
┌─────────────────────────────────────────────────────────┐
│                     VISTA (View)                        │
│  - MainView.java                                        │
│  - ClientePanel.java                                    │
│  - ReservaPanel.java                                    │
│  - FacturaPanel.java                                    │
└──────────────────┬──────────────────────────────────────┘
                   │ Interacción Usuario
                   ▼
┌─────────────────────────────────────────────────────────┐
│                CONTROLADOR (Controller)                 │
│  - ClienteController.java                               │
│  - ReservaController.java                               │
│  - FacturaController.java                               │
└──────────────────┬──────────────────────────────────────┘
                   │ Lógica de Negocio
                   ▼
┌─────────────────────────────────────────────────────────┐
│                   MODELO (Model)                        │
│  ENTIDADES:              DAOs:                          │
│  - Cliente.java          - ClienteDAO.java              │
│  - Reserva.java          - ReservaDAO.java              │
│  - Factura.java          - FacturaDAO.java              │
└──────────────────┬──────────────────────────────────────┘
                   │ Persistencia
                   ▼
┌─────────────────────────────────────────────────────────┐
│              BASE DE DATOS MySQL                        │
│  - travel_agency_g8                                     │
│    • cliente                                            │
│    • reserva                                            │
│    • factura                                            │
└─────────────────────────────────────────────────────────┘
```

### 2.2 Estructura de Paquetes

```
src/main/java/
├── com.ug.proyecto_g8/          # Clase principal
├── controlador/                  # Controladores MVC
│   ├── ClienteController.java
│   ├── ReservaController.java
│   └── FacturaController.java
├── modelo/                       # Modelos y DAOs
│   ├── Cliente.java
│   ├── ClienteDAO.java
│   ├── Reserva.java
│   ├── ReservaDAO.java
│   ├── Factura.java
│   └── FacturaDAO.java
├── util/                         # Utilidades
│   ├── ConexionMySQL.java
│   ├── ValidacionCliente.java
│   └── ValidacionReserva.java
└── vista/                        # Interfaces gráficas
    ├── MainView.java
    ├── ClientePanel.java
    ├── ReservaPanel.java
    └── FacturaPanel.java
```

### 2.3 Modelo de Base de Datos

```sql
┌─────────────────────┐
│      CLIENTE        │
├─────────────────────┤
│ id_cliente (PK)     │
│ nombre              │
│ apellido            │
│ dni (UNIQUE)        │
│ telefono            │
│ email               │
│ direccion           │
└──────────┬──────────┘
           │ 1
           │
           │ N
┌──────────▼──────────┐
│      RESERVA        │
├─────────────────────┤
│ id_reserva (PK)     │
│ id_cliente (FK)     │
│ fecha_reserva       │
│ destino             │
│ fecha_viaje         │
│ precio              │
│ estado              │
└──────────┬──────────┘
           │ 1
           │
           │ N
┌──────────▼──────────┐
│      FACTURA        │
├─────────────────────┤
│ id_factura (PK)     │
│ id_reserva (FK)     │
│ fecha_emision       │
│ monto_total         │
│ estado              │
└─────────────────────┘
```

---

## 3. Requisitos del Sistema

### 3.1 Requisitos de Hardware

| Componente | Mínimo | Recomendado |
|------------|--------|-------------|
| Procesador | Intel Core i3 / AMD Ryzen 3 | Intel Core i5 / AMD Ryzen 5 |
| Memoria RAM | 4 GB | 8 GB o más |
| Disco Duro | 2 GB libres | 5 GB libres |
| Resolución | 1024x768 | 1920x1080 |

### 3.2 Requisitos de Software

#### Sistema Operativo
- Windows 10/11
- Linux (Ubuntu 20.04+, Fedora, etc.)
- macOS 11+

#### Software Requerido

1. **Java Development Kit (JDK) 17**
   - Descargar desde: https://adoptium.net/

2. **Apache Maven 3.9+**
   - Descargar desde: https://maven.apache.org/download.cgi

3. **MySQL Server 8.0+**
   - Descargar desde: https://dev.mysql.com/downloads/mysql/

4. **Git** (opcional, para control de versiones)
   - Descargar desde: https://git-scm.com/

5. **Jenkins** (opcional, para CI/CD)
   - Descargar desde: https://www.jenkins.io/download/

### 3.3 Verificación de Instalaciones

```powershell
# Verificar Java
java -version
# Salida esperada: openjdk version "17.x.x"

# Verificar Maven
mvn -version
# Salida esperada: Apache Maven 3.9.x

# Verificar MySQL
mysql --version
# Salida esperada: mysql Ver 8.0.x

# Verificar Git
git --version
# Salida esperada: git version 2.x.x
```

---

## 4. Instalación y Configuración

### 4.1 Instalación de Java JDK 17

#### Windows:
1. Descargar el instalador desde https://adoptium.net/
2. Ejecutar el instalador `.msi`
3. Seguir el asistente de instalación
4. Configurar variable de entorno `JAVA_HOME`:
   ```powershell
   # Abrir PowerShell como Administrador
   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot', 'Machine')
   ```

#### Linux:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

### 4.2 Instalación de Maven

#### Windows:
1. Descargar el archivo `.zip` desde Maven
2. Extraer en `C:\Program Files\Apache\maven`
3. Agregar al PATH:
   ```powershell
   [System.Environment]::SetEnvironmentVariable('Path', $env:Path + ';C:\Program Files\Apache\maven\bin', 'Machine')
   ```

#### Linux:
```bash
sudo apt install maven
mvn -version
```

### 4.3 Instalación de MySQL

#### Windows:
1. Descargar MySQL Installer
2. Seleccionar "Developer Default"
3. Configurar root password
4. Iniciar el servicio MySQL

#### Linux:
```bash
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
sudo mysql_secure_installation
```

### 4.4 Configuración del Proyecto

#### Paso 1: Clonar/Descargar el Proyecto

```bash
# Si usa Git
git clone <repository-url>
cd simple_java_CRUD_S8

# O descargar y extraer el ZIP
```

#### Paso 2: Configurar Conexión a Base de Datos

Editar el archivo `src/main/java/util/ConexionMySQL.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/travel_agency_g8";
private static final String USER = "tu_usuario";      // ← Cambiar aquí
private static final String PASSWORD = "tu_password";  // ← Cambiar aquí
```

⚠️ **IMPORTANTE:** Nunca subir credenciales reales a repositorios públicos.

#### Paso 3: Configurar Maven

El archivo `pom.xml` ya está configurado. Verificar:

```xml
<groupId>com.ug</groupId>
<artifactId>proyecto_g8</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>
```

---

## 5. Gestión de Base de Datos

### 5.1 Creación de la Base de Datos

#### Opción 1: Usando MySQL Workbench
1. Abrir MySQL Workbench
2. Conectar al servidor MySQL
3. Crear nueva query
4. Ejecutar el script SQL del README.md

#### Opción 2: Línea de Comandos

```bash
# Conectar a MySQL
mysql -u root -p

# Ejecutar script
source /path/to/database_script.sql

# O copiar y pegar directamente:
```

```sql
CREATE DATABASE travel_agency_g8;
USE travel_agency_g8;

-- Tabla Cliente
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200)
);

-- Tabla Reserva
CREATE TABLE reserva (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha_reserva DATE NOT NULL,
    destino VARCHAR(100) NOT NULL,
    fecha_viaje DATE NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- Tabla Factura
CREATE TABLE factura (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    fecha_emision DATE NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'PAGADA', 'ANULADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva)
);
```

### 5.2 Verificación de Tablas

```sql
-- Ver todas las tablas
SHOW TABLES;

-- Describir estructura de cada tabla
DESCRIBE cliente;
DESCRIBE reserva;
DESCRIBE factura;

-- Verificar relaciones
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE
    REFERENCED_TABLE_SCHEMA = 'travel_agency_g8';
```

### 5.3 Datos de Prueba (Opcional)

```sql
-- Insertar clientes de prueba
INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES
('Juan', 'Pérez', '12345678', '555-1234', 'juan.perez@email.com', 'Calle 1, Ciudad'),
('María', 'García', '87654321', '555-5678', 'maria.garcia@email.com', 'Avenida 2, Ciudad'),
('Carlos', 'López', '11223344', '555-9012', 'carlos.lopez@email.com', 'Boulevard 3, Ciudad');

-- Insertar reservas de prueba
INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES
(1, CURDATE(), 'París, Francia', DATE_ADD(CURDATE(), INTERVAL 30 DAY), 1500.00, 'CONFIRMADA'),
(2, CURDATE(), 'Roma, Italia', DATE_ADD(CURDATE(), INTERVAL 45 DAY), 1200.00, 'PENDIENTE');

-- Insertar facturas de prueba
INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES
(1, CURDATE(), 1500.00, 'PAGADA'),
(2, CURDATE(), 1200.00, 'PENDIENTE');
```

### 5.4 Respaldo y Restauración

#### Crear Respaldo

```bash
# Respaldo completo de la base de datos
mysqldump -u root -p travel_agency_g8 > backup_travel_agency_$(date +%Y%m%d).sql

# Respaldo solo de estructura (sin datos)
mysqldump -u root -p --no-data travel_agency_g8 > backup_structure.sql

# Respaldo solo de datos
mysqldump -u root -p --no-create-info travel_agency_g8 > backup_data.sql
```

#### Restaurar Respaldo

```bash
mysql -u root -p travel_agency_g8 < backup_travel_agency_20260201.sql
```

---

## 6. Operación de la Aplicación

### 6.1 Compilación del Proyecto

```bash
# Navegar al directorio del proyecto
cd simple_java_CRUD_S8

# Compilar el proyecto
mvn clean compile

# Salida esperada:
# [INFO] BUILD SUCCESS
```

### 6.2 Ejecución de la Aplicación

#### Método 1: Con Maven

```bash
mvn clean compile exec:java -Dexec.mainClass="vista.MainView"
```

#### Método 2: Generar JAR y Ejecutar

```bash
# Empaquetar aplicación
mvn clean package

# Ejecutar JAR
java -cp target/proyecto_g8-1.0-SNAPSHOT.jar vista.MainView
```

#### Método 3: Desde IDE (Eclipse/IntelliJ/NetBeans)

1. Importar proyecto Maven
2. Buscar clase `vista.MainView`
3. Click derecho → Run As → Java Application

### 6.3 Uso de la Interfaz Gráfica

#### 6.3.1 Ventana Principal

La aplicación se abre con tres pestañas principales:

```
┌────────────────────────────────────────────────────┐
│  [Clientes] [Reservas] [Facturas]                  │
├────────────────────────────────────────────────────┤
│                                                    │
│              CONTENIDO DE LA PESTAÑA               │
│                                                    │
└────────────────────────────────────────────────────┘
```

#### 6.3.2 Gestión de Clientes

**Operaciones Disponibles:**

1. **Agregar Cliente**
   - Click en botón "Agregar Cliente"
   - Completar formulario:
     - Nombre (obligatorio)
     - Apellido (obligatorio)
     - DNI (único, obligatorio)
     - Teléfono
     - Email
     - Dirección
   - Click en "Guardar"

2. **Consultar Cliente**
   - Seleccionar cliente de la tabla
   - Ver detalles en panel derecho

3. **Actualizar Cliente**
   - Seleccionar cliente de la tabla
   - Click en "Editar"
   - Modificar campos
   - Click en "Guardar"

4. **Eliminar Cliente**
   - Seleccionar cliente de la tabla
   - Click en "Eliminar"
   - Confirmar eliminación

5. **Ver Reservas del Cliente**
   - Seleccionar cliente
   - Click en "Ver Reservas"
   - Se abre pestaña de Reservas filtrada

6. **Reservar Vuelo**
   - Seleccionar cliente
   - Click en "Reservar Vuelo"
   - Se abre formulario de nueva reserva

#### 6.3.3 Gestión de Reservas

**Operaciones Disponibles:**

1. **Crear Reserva**
   - Click en "Nueva Reserva"
   - Seleccionar cliente
   - Ingresar:
     - Fecha de reserva
     - Destino
     - Fecha de viaje
     - Precio
   - Estado inicial: PENDIENTE

2. **Actualizar Reserva**
   - Seleccionar reserva
   - Modificar datos
   - Cambiar estado: PENDIENTE → CONFIRMADA → CANCELADA

3. **Eliminar Reserva**
   - Seleccionar reserva
   - Click en "Eliminar"

4. **Generar Factura**
   - Seleccionar reserva confirmada
   - Click en "Generar Factura"
   - Se crea automáticamente en pestaña Facturas

#### 6.3.4 Gestión de Facturas

**Operaciones Disponibles:**

1. **Ver Facturas**
   - Lista de todas las facturas
   - Filtrar por estado: PENDIENTE, PAGADA, ANULADA

2. **Actualizar Estado de Factura**
   - Seleccionar factura
   - Cambiar estado
   - Guardar

3. **Consultar Detalles**
   - Ver información de reserva asociada
   - Ver datos del cliente

### 6.4 Flujo de Trabajo Típico

```
1. REGISTRAR CLIENTE
   ↓
2. CREAR RESERVA para el cliente
   ↓
3. CONFIRMAR RESERVA
   ↓
4. GENERAR FACTURA
   ↓
5. MARCAR FACTURA como PAGADA
```

### 6.5 Validaciones Implementadas

#### Validación de Cliente
- ✅ DNI único en el sistema
- ✅ Nombre y apellido obligatorios
- ✅ Formato de email válido
- ✅ Longitud máxima de campos

#### Validación de Reserva
- ✅ Fecha de viaje posterior a fecha de reserva
- ✅ Precio positivo
- ✅ Cliente debe existir
- ✅ Destino obligatorio

#### Validación de Factura
- ✅ Reserva debe existir
- ✅ Monto total positivo
- ✅ No duplicar facturas para misma reserva

---

## 7. Gestión Maven

### 7.1 Configuración del POM

El archivo `pom.xml` es el corazón de Maven. Componentes principales:

```xml
<project>
  <groupId>com.ug</groupId>                    <!-- Identificador del grupo -->
  <artifactId>proyecto_g8</artifactId>          <!-- Nombre del proyecto -->
  <version>1.0-SNAPSHOT</version>               <!-- Versión -->
  <packaging>jar</packaging>                    <!-- Tipo de empaquetado -->
</project>
```

### 7.2 Dependencias del Proyecto

| Dependencia | GroupId | ArtifactId | Versión | Propósito |
|-------------|---------|------------|---------|-----------|
| **MySQL Connector** | mysql | mysql-connector-java | 8.0.28 | Conexión a base de datos MySQL |
| **SwingX** | org.swinglabs | swingx | 1.6.1 | Componentes avanzados para interfaz gráfica Swing |
| **JUnit Jupiter** | org.junit.jupiter | junit-jupiter | 5.9.2 | Framework de pruebas unitarias (scope: test) |

#### Documentación de Dependencias

**MySQL Connector Java 8.0.28:**
- **Propósito:** Driver JDBC para conectar aplicaciones Java con bases de datos MySQL
- **Uso:** Permite ejecutar consultas SQL y gestionar transacciones
- **Alcance:** runtime (disponible en compilación y ejecución)

**SwingX 1.6.1:**
- **Propósito:** Biblioteca de componentes extendidos para Swing
- **Uso:** Proporciona widgets mejorados como tablas, calendarios, validadores
- **Alcance:** compile (requerido en compilación y ejecución)

**JUnit Jupiter 5.9.2:**
- **Propósito:** Framework moderno de pruebas unitarias para Java
- **Uso:** Crear y ejecutar pruebas automatizadas del código
- **Alcance:** test (solo disponible durante pruebas)

### 7.3 Ciclo de Vida Maven

Maven define fases de construcción estándar:

#### 7.3.1 Fase: validate

```bash
mvn validate
```

**Propósito:** Validar que el proyecto es correcto y toda la información necesaria está disponible.

**Verifica:**
- ✅ pom.xml bien formado
- ✅ Dependencias declaradas correctamente
- ✅ Propiedades configuradas

**Salida esperada:**
```
[INFO] BUILD SUCCESS
```

#### 7.3.2 Fase: compile

```bash
mvn compile
```

**Propósito:** Compilar el código fuente del proyecto.

**Acciones:**
- Descarga dependencias si es necesario
- Compila archivos `.java` a `.class`
- Coloca archivos compilados en `target/classes/`

**Salida esperada:**
```
[INFO] Compiling X source files to target/classes
[INFO] BUILD SUCCESS
```

#### 7.3.3 Fase: test

```bash
mvn test
```

**Propósito:** Ejecutar pruebas unitarias con framework de testing.

**Acciones:**
- Compila código de pruebas en `src/test/java`
- Ejecuta todas las clases `*Test.java`
- Genera reportes en `target/surefire-reports/`

**Salida esperada:**
```
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0

[INFO] BUILD SUCCESS
```

**Reportes Generados:**
- `TEST-*.xml` - Resultados en formato XML
- `*.txt` - Resultados en texto plano

#### 7.3.4 Fase: package

```bash
mvn package
```

**Propósito:** Empaquetar código compilado en formato distribuible (JAR).

**Acciones:**
- Ejecuta compile y test
- Crea archivo JAR en `target/`
- Nombre: `proyecto_g8-1.0-SNAPSHOT.jar`

**Salida esperada:**
```
[INFO] Building jar: target/proyecto_g8-1.0-SNAPSHOT.jar
[INFO] BUILD SUCCESS
```

#### 7.3.5 Fase: install

```bash
mvn install
```

**Propósito:** Instalar el paquete en el repositorio local Maven (~/.m2/repository).

**Acciones:**
- Ejecuta todas las fases anteriores
- Copia JAR a repositorio local
- Permite usar el proyecto como dependencia en otros proyectos

**Salida esperada:**
```
[INFO] Installing target/proyecto_g8-1.0-SNAPSHOT.jar to ~/.m2/repository/...
[INFO] BUILD SUCCESS
```

### 7.4 Comandos Maven Comunes

#### Limpiar Proyecto

```bash
mvn clean
```
Elimina directorio `target/` y todos los archivos compilados.

#### Compilar sin Tests

```bash
mvn clean compile -DskipTests
```

#### Empaquetar sin Tests

```bash
mvn clean package -DskipTests
```

#### Ejecutar Test Específico

```bash
mvn test -Dtest=ClienteDAOTest
```

#### Ver Dependencias del Proyecto

```bash
mvn dependency:tree
```

**Salida:**
```
com.ug:proyecto_g8:jar:1.0-SNAPSHOT
+- mysql:mysql-connector-java:jar:8.0.28:compile
+- org.swinglabs:swingx:jar:1.6.1:compile
\- org.junit.jupiter:junit-jupiter:jar:5.9.2:test
```

#### Actualizar Dependencias

```bash
mvn clean install -U
```

La opción `-U` fuerza actualización de dependencias.

#### Generar Documentación Javadoc

```bash
mvn javadoc:javadoc
```

Genera documentación HTML en `target/site/apidocs/`

### 7.5 Plugins Maven Configurados

#### Maven Compiler Plugin 3.10.1

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.10.1</version>
    <configuration>
        <release>17</release>
    </configuration>
</plugin>
```

**Propósito:** Compilar código Java con versión específica.

#### Maven Surefire Plugin 3.0.0

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
</plugin>
```

**Propósito:** Ejecutar pruebas unitarias durante fase `test`.

#### JaCoCo Maven Plugin 0.8.11

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
</plugin>
```

**Propósito:** Generar reportes de cobertura de código.

**Uso:**
```bash
mvn clean test jacoco:report
```

**Reporte generado en:** `target/site/jacoco/index.html`


## 8. Pruebas y Calidad de Código

### 8.1 Estructura de Pruebas

```
src/test/java/
├── modelo/
│   ├── ClienteDAOTest.java      # Pruebas DAO de Cliente
│   ├── FacturaDAOTest.java      # Pruebas DAO de Factura
│   ├── ReservaDAOTest.java      # Pruebas DAO de Reserva
│   ├── TestClienteDAO.java      # Pruebas adicionales
│   ├── TestFacturaDAO.java      # Pruebas adicionales
│   └── TestReservaDAO.java      # Pruebas adicionales
└── util/
    └── TestConexionMySQL.java   # Pruebas de conexión
```

### 8.2 Ejecución de Pruebas

#### Ejecutar Todas las Pruebas

```bash
mvn test
```

**Salida:**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running modelo.ClienteDAOTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running modelo.ReservaDAOTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running modelo.FacturaDAOTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

#### Ejecutar Prueba Específica

```bash
# Una clase de prueba
mvn test -Dtest=ClienteDAOTest

# Un método específico
mvn test -Dtest=ClienteDAOTest#testAgregarCliente

# Múltiples clases
mvn test -Dtest=ClienteDAOTest,ReservaDAOTest
```

#### Pruebas con Cobertura

```bash
mvn clean test jacoco:report
```

Abrir reporte: `target/site/jacoco/index.html`

### 8.3 Interpretación de Reportes de Prueba

#### Reporte Surefire (TXT)

**Ubicación:** `target/surefire-reports/modelo.ClienteDAOTest.txt`

```
Test set: modelo.ClienteDAOTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.234 s
```

#### Reporte Surefire (XML)

**Ubicación:** `target/surefire-reports/TEST-modelo.ClienteDAOTest.xml`

```xml
<testsuite name="modelo.ClienteDAOTest" tests="5" failures="0" errors="0" skipped="0">
    <testcase name="testAgregarCliente" classname="modelo.ClienteDAOTest" time="0.245"/>
    <testcase name="testConsultarCliente" classname="modelo.ClienteDAOTest" time="0.123"/>
    ...
</testsuite>
```

### 8.4 Cobertura de Código con JaCoCo

#### Generar Reporte

```bash
mvn clean test jacoco:report
```

#### Abrir Reporte HTML

```bash
# Windows
start target/site/jacoco/index.html

# Linux/Mac
open target/site/jacoco/index.html
```

#### Métricas de Cobertura

**Niveles de Cobertura:**

| Paquete | Instrucciones | Ramas | Líneas | Métodos | Clases |
|---------|--------------|-------|--------|---------|--------|
| modelo  | 75% 🟢 | 65% 🟡 | 80% 🟢 | 70% 🟢 | 100% 🟢 |
| controlador | 70% 🟢 | 60% 🟡 | 75% 🟢 | 65% 🟡 | 100% 🟢 |
| util | 85% 🟢 | 70% 🟢 | 90% 🟢 | 80% 🟢 | 100% 🟢 |
| vista | 45% 🔴 | 30% 🔴 | 50% 🔴 | 40% 🔴 | 100% 🟢 |

🟢 Buena cobertura (>70%)  
🟡 Cobertura aceptable (60-70%)  
🔴 Cobertura baja (<60%)

**Nota:** Vista tiene cobertura baja porque las interfaces gráficas son difíciles de probar automáticamente.

### 8.5 Mejores Prácticas de Testing

#### 1. Nomenclatura de Pruebas

```java
@Test
public void testAgregarCliente() { }  // ✅ Correcto

@Test
public void test1() { }  // ❌ Incorrecto - no descriptivo
```

#### 2. Estructura AAA (Arrange-Act-Assert)

```java
@Test
public void testConsultarCliente() {
    // Arrange - Preparar datos
    Cliente cliente = new Cliente();
    cliente.setNombre("Juan");
    clienteDAO.agregarCliente(cliente);
    
    // Act - Ejecutar acción
    Cliente resultado = clienteDAO.consultarCliente(cliente.getIdCliente());
    
    // Assert - Verificar resultado
    assertNotNull(resultado);
    assertEquals("Juan", resultado.getNombre());
}
```

#### 3. Independencia de Pruebas

- ✅ Cada prueba debe ser independiente
- ✅ No depender del orden de ejecución
- ✅ Limpiar datos después de cada prueba

```java
@AfterEach
public void limpiarDatos() {
    // Eliminar datos de prueba
}
```

#### 4. Usar Assertions Significativos

```java
// ❌ Poco informativo
assertTrue(cliente != null);

// ✅ Más claro
assertNotNull(cliente, "El cliente no debería ser null");

// ✅ Mensajes personalizados
assertEquals("Juan", cliente.getNombre(), 
    "El nombre del cliente debería ser 'Juan'");
```

### 8.6 Análisis de Calidad de Código

#### Checkstyle (Estilo de Código)

Configuración en `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.1</version>
</plugin>
```

**Ejecutar:**
```bash
mvn checkstyle:check
```

**Verifica:**
- Convenciones de nombres
- Indentación
- Imports no usados
- Complejidad de métodos

#### SpotBugs (Detección de Bugs)

**Agregar al pom.xml:**
```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.7.3.6</version>
</plugin>
```

**Ejecutar:**
```bash
mvn spotbugs:check
```

---

## 9. Mantenimiento y Solución de Problemas

### 9.1 Problemas Comunes y Soluciones

#### Error: "Access denied for user"

**Síntoma:**
```
java.sql.SQLException: Access denied for user 'root'@'localhost'
```

**Solución:**
1. Verificar credenciales en `ConexionMySQL.java`
2. Resetear password de MySQL:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nueva_password';
FLUSH PRIVILEGES;
```

#### Error: "Communications link failure"

**Síntoma:**
```
Communications link failure: java.net.ConnectException: Connection refused
```

**Solución:**
1. Verificar MySQL corriendo:
```powershell
Get-Service MySQL80
# Si no está corriendo:
Start-Service MySQL80
```

2. Verificar puerto:
```sql
SHOW VARIABLES LIKE 'port';
```

#### Error: "Duplicate entry for key 'dni'"

**Síntoma:**
```
java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '12345678' for key 'dni'
```

**Solución:**
- El DNI ya existe en la base de datos
- Usar DNI diferente o actualizar registro existente

#### Error: "BUILD FAILURE - Tests failed"

**Síntoma:**
```
Tests run: 15, Failures: 2, Errors: 1, Skipped: 0
```

**Solución:**
1. Ver detalles:
```bash
cat target/surefire-reports/modelo.ClienteDAOTest.txt
```

2. Ejecutar test específico:
```bash
mvn test -Dtest=ClienteDAOTest
```

3. Revisar stacktrace en Console Output

#### Error: "Java version mismatch"

**Síntoma:**
```
Invalid target release: 17
```

**Solución:**
1. Verificar Java instalado:
```bash
java -version
```

2. Configurar JAVA_HOME correctamente
3. En pom.xml, ajustar:
```xml
<maven.compiler.release>17</maven.compiler.release>
```

### 9.2 Logs y Depuración

#### Habilitar Logs en Consola

Agregar a las clases:

```java
import java.util.logging.*;

public class ClienteDAO {
    private static final Logger LOGGER = Logger.getLogger(ClienteDAO.class.getName());
    
    public boolean agregarCliente(Cliente cliente) {
        LOGGER.info("Agregando cliente: " + cliente.getNombre());
        // ...
    }
}
```

#### Logs de MySQL

**Ver logs de consultas:**

```sql
-- Habilitar log de consultas
SET GLOBAL general_log = 'ON';
SET GLOBAL log_output = 'TABLE';

-- Ver últimas consultas
SELECT * FROM mysql.general_log 
ORDER BY event_time DESC 
LIMIT 50;
```

#### Depuración en IDE

**Eclipse/IntelliJ:**
1. Colocar breakpoints (click en margen izquierdo)
2. Click derecho → Debug As → Java Application
3. Usar controles de depuración:
   - F5: Step Into
   - F6: Step Over
   - F7: Step Return
   - F8: Resume

### 9.3 Mantenimiento de Base de Datos

#### Optimizar Tablas

```sql
OPTIMIZE TABLE cliente, reserva, factura;
```

#### Analizar Rendimiento

```sql
-- Ver consultas lentas
SHOW FULL PROCESSLIST;

-- Estadísticas de tablas
SHOW TABLE STATUS FROM travel_agency_g8;

-- Índices de una tabla
SHOW INDEX FROM cliente;
```

#### Limpiar Datos Antiguos

```sql
-- Eliminar reservas canceladas antiguas (> 1 año)
DELETE FROM reserva 
WHERE estado = 'CANCELADA' 
AND fecha_reserva < DATE_SUB(NOW(), INTERVAL 1 YEAR);

-- Eliminar facturas anuladas antiguas
DELETE FROM factura 
WHERE estado = 'ANULADA' 
AND fecha_emision < DATE_SUB(NOW(), INTERVAL 1 YEAR);
```

### 9.4 Actualización de Dependencias

#### Ver Dependencias Desactualizadas

```bash
mvn versions:display-dependency-updates
```

#### Actualizar Dependencias

Editar `pom.xml`:

```xml
<!-- Actualizar MySQL Connector -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>  <!-- versión actualizada -->
</dependency>
```

Luego:
```bash
mvn clean install -U
```

### 9.5 Gestión de Versiones

#### Crear Nueva Versión

```bash
# Actualizar versión en pom.xml
mvn versions:set -DnewVersion=1.1.0

# Confirmar cambio
mvn versions:commit

# O revertir
mvn versions:revert
```

#### Tagging en Git

```bash
# Crear tag
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push tag
git push origin v1.0.0

# Ver tags
git tag -l
```

### 9.6 Monitoreo de la Aplicación

#### Métricas a Monitorear

1. **Base de Datos:**
   - Número de conexiones activas
   - Tiempo de respuesta de queries
   - Tamaño de tablas

```sql
-- Conexiones activas
SHOW STATUS LIKE 'Threads_connected';

-- Tamaño de base de datos
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'travel_agency_g8'
GROUP BY table_schema;
```

2. **Aplicación:**
   - Uso de memoria
   - Tiempo de respuesta de operaciones
   - Errores/excepciones

3. **Jenkins:**
   - Tasa de éxito de builds
   - Tiempo promedio de build
   - Tendencia de cobertura de código

---

## 10. Glosario Técnico

### Términos de Arquitectura

**MVC (Model-View-Controller):**
Patrón de diseño que separa la aplicación en tres componentes: Modelo (datos), Vista (interfaz), Controlador (lógica).

**DAO (Data Access Object):**
Patrón que abstrae el acceso a datos, proporcionando una interfaz para operaciones CRUD sin exponer detalles de la base de datos.

**CRUD:**
Create (Crear), Read (Leer), Update (Actualizar), Delete (Eliminar) - operaciones básicas sobre datos.

**JavaBean:**
Clase Java que sigue convenciones: constructor sin parámetros, getters/setters, serializable.

### Términos de Maven

**POM (Project Object Model):**
Archivo XML (`pom.xml`) que contiene configuración y dependencias del proyecto Maven.

**Artifact:**
Salida del proyecto (JAR, WAR, etc.) identificado por groupId:artifactId:version.

**Dependency:**
Biblioteca externa requerida por el proyecto.

**Scope:**
Alcance de una dependencia (compile, test, runtime, provided).

**Repository:**
Ubicación donde Maven almacena y recupera artefactos (.m2/repository local, Maven Central remoto).

### Términos de Testing

**Unit Test:**
Prueba que verifica funcionamiento de una unidad mínima de código (método/clase).

**Test Coverage:**
Porcentaje de código ejecutado durante las pruebas.

**Assertion:**
Verificación de que un resultado esperado coincide con el resultado real.

**Mock Object:**
Objeto simulado que imita comportamiento de objetos reales en pruebas.

**Test Suite:**
Conjunto de tests que se ejecutan juntos.

### Términos de CI/CD

**CI (Continuous Integration):**
Práctica de integrar cambios de código frecuentemente, con builds y tests automáticos.

**CD (Continuous Deployment):**
Extensión de CI que automatiza el despliegue a producción.

**Pipeline:**
Secuencia automatizada de pasos (build, test, deploy) en proceso CI/CD.

**Stage:**
Fase dentro de un pipeline (ej: Build, Test, Deploy).

**Build:**
Proceso de compilar código fuente en ejecutable.

**Artifact:**
Resultado de un build (JAR, reportes, etc.).

### Términos de Base de Datos

**Primary Key (PK):**
Identificador único de cada registro en una tabla.

**Foreign Key (FK):**
Campo que referencia la Primary Key de otra tabla, estableciendo relación.

**JDBC (Java Database Connectivity):**
API de Java para conectar y ejecutar queries en bases de datos.

**Connection Pool:**
Conjunto reutilizable de conexiones a base de datos para mejorar rendimiento.

**Transaction:**
Secuencia de operaciones que se ejecutan como unidad atómica (todo o nada).

### Términos de Java

**Swing:**
Framework de Java para crear interfaces gráficas de usuario (GUI).

**JPanel:**
Contenedor Swing para agrupar componentes de interfaz.

**JTabbedPane:**
Componente Swing que organiza contenido en pestañas.

**Exception:**
Evento anormal que ocurre durante ejecución y interrumpe el flujo normal.

**Try-Catch:**
Estructura para manejar excepciones y evitar caídas de aplicación.

---

## Anexos

### A. Comandos Rápidos de Referencia

```bash
# MAVEN
mvn validate                    # Validar proyecto
mvn clean compile               # Compilar
mvn test                        # Ejecutar pruebas
mvn package                     # Empaquetar JAR
mvn install                     # Instalar en repo local
mvn clean install -DskipTests   # Instalar sin tests

# MYSQL
mysql -u root -p                           # Conectar a MySQL
mysqldump -u root -p DB > backup.sql       # Respaldar
mysql -u root -p DB < backup.sql           # Restaurar
SHOW DATABASES;                            # Listar bases de datos
USE travel_agency_g8;                      # Seleccionar base de datos
SHOW TABLES;                               # Listar tablas

# GIT
git status                      # Ver estado
git add .                       # Añadir cambios
git commit -m "mensaje"         # Commit
git push origin main            # Subir cambios
git pull                        # Descargar cambios
git log --oneline               # Ver historial

# JAVA
java -version                   # Ver versión Java
javac MiClase.java              # Compilar clase
java MiClase                    # Ejecutar clase
java -jar app.jar               # Ejecutar JAR
```

**Desarrollado por:**  
Universidad de Guayaquil - Grupo 8

**Proyecto:**  
Sistema de Gestión de Agencia de Viajes

**Repositorio:**  
[https://github.com/Btlguse/simple_java_CRUD_S8 ]

**Documentación Adicional:**
- [README.md](README.md) - Información general del proyecto
- [JENKINS_SETUP.md](JENKINS_SETUP.md) - Configuración detallada de Jenkins

---

**Fin del Manual de Operaciones**  
*Versión 1.0 - Febrero 2026*

