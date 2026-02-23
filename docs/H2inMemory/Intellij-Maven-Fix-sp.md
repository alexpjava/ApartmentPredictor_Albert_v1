# Fix: IntelliJ no reconoce proyecto Java (Maven + Spring Boot)

## Contexto del problema

El proyecto tenía la siguiente estructura de directorios. El repositorio Git estaba inicializado en la carpeta padre (`ApartmentPredictor_Albert_v1`), pero el proyecto Java real estaba en la subcarpeta `ApartmentPredictor`:

```
C:\Users\Alumne_mati1\AppWeb25_Local\
└── ApartmentPredictor_Albert_v1\          ← carpeta abierta en IntelliJ (raíz del Git)
    ├── .git\
    └── ApartmentPredictor\                ← proyecto Java real aquí
        ├── .idea\
        ├── src\
        │   └── main\
        │       └── java\
        │           └── com\example\...
        ├── target\
        ├── mvnw
        ├── mvnw.cmd
        ├── pom.xml
        └── apartment-predictor.iml
```

## Síntomas

- Al abrir la clase principal (`@SpringBootApplication`) no aparecía el botón verde de **Run**
- No se podían crear nuevos ficheros `.java`
- IntelliJ no reconocía el proyecto como un proyecto Java/Maven

## Diagnóstico mediante el log de IntelliJ

El log de IntelliJ se encuentra en:
```
C:\Users\Alumne_mati1\AppData\Local\JetBrains\IntelliJIdea2025.3\log\idea.log
```

Dentro del log encontramos estos errores repetidos que indicaban el problema raíz:

```
WARN - #o.j.i.maven - exception reading wrapper url
WARN - #o.j.i.maven - Suppressed a frequent exception logged for the 5th time:
C:\Users\Alumne_mati1\AppWeb25_Local\ApartmentPredictor_Albert_v1\ApartmentPredictor\.mvn\wrapper\maven-wrapper.properties
```

Esto indicaba que IntelliJ buscaba el fichero `maven-wrapper.properties` pero no lo encontraba porque **la carpeta `.mvn` no existía en el proyecto**.

## Causa raíz

El proyecto Spring Boot fue creado sin la carpeta `.mvn/wrapper/`, que normalmente se genera automáticamente con Spring Initializr. Esta carpeta contiene el fichero `maven-wrapper.properties`, que le dice a IntelliJ qué versión de Maven usar para gestionar el proyecto. Sin él, IntelliJ no puede importar correctamente el módulo Maven.

La estructura que **faltaba** era:
```
ApartmentPredictor\
└── .mvn\                                  ← no existía
    └── wrapper\
        └── maven-wrapper.properties       ← no existía
```

## Solución paso a paso

### Paso 1: Verificar que la carpeta .mvn no existe

Abre una terminal (Git Bash o PowerShell) dentro de la carpeta del proyecto y lista el contenido:

```bash
ls -la
```

Resultado que confirmaba el problema (no aparece `.mvn`):
```
drwxr-xr-x  .idea/
-rw-r--r--  .gitattributes
-rw-r--r--  .gitignore
-rw-r--r--  apartment-predictor.iml
-rwxr-xr-x  mvnw*
-rw-r--r--  mvnw.cmd
-rw-r--r--  pom.xml
drwxr-xr-x  src/
drwxr-xr-x  target/
```

### Paso 2: Crear la carpeta y el fichero maven-wrapper.properties

Ejecuta este comando desde dentro de la carpeta `ApartmentPredictor`:

```bash
mkdir -p .mvn/wrapper && echo "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip" > .mvn/wrapper/maven-wrapper.properties
```

### Paso 3: Verificar que el fichero se ha creado correctamente

```bash
cat .mvn/wrapper/maven-wrapper.properties
```

Resultado esperado:
```
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip
```

La estructura ahora correcta:
```
ApartmentPredictor\
└── .mvn\
    └── wrapper\
        └── maven-wrapper.properties       ← creado correctamente
```

### Paso 4: Recargar Maven en IntelliJ

1. En IntelliJ ve a **View → Tool Windows → Maven** para abrir el panel de Maven
2. En el panel de Maven, haz clic en el icono de recargar ↻ (dos flechas circulares en la parte superior del panel)
3. Espera a que la barra de progreso (abajo del IDE) desaparezca completamente

### Paso 5: Verificar que funciona

Abre la clase principal con `@SpringBootApplication`. Ahora debería aparecer el triángulo verde ▶ de Run a la izquierda del método `main`. Al hacer clic debería arrancar Spring Boot y ver en la consola algo como:

```
Started ApartmentPredictorApplication in 3.5 seconds (JVM running for 4.2)
```

## Referencia: pom.xml del proyecto

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>apartment-predictor</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <properties>
        <java.version>21</java.version>
    </properties>
    <!-- dependencias: Spring Boot Web, JPA, H2, DevTools, DeepLearning4J -->
</project>
```

## Lecciones aprendidas

- Cuando IntelliJ no reconoce un proyecto Java, el fichero `idea.log` es la primera herramienta de diagnóstico. Buscar `WARN` y `ERROR` relacionados con `maven` suele revelar el problema.
- Al abrir IntelliJ desde una **carpeta padre** (por ejemplo la raíz del Git), hay que asegurarse de que el proyecto Maven esté correctamente importado como módulo.
- Si el proyecto Spring Boot no tiene la carpeta `.mvn/wrapper/`, Maven no sabe qué versión usar y falla silenciosamente. Crearla manualmente con la `distributionUrl` correcta soluciona el problema.
- Spring Initializr genera esta carpeta automáticamente, pero puede perderse si el proyecto se clona parcialmente o se crea de forma manual.
