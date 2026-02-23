# Fix: Consolidar .gitignore en la carpeta raíz del Git

## Contexto del problema

El proyecto tenía el Git inicializado en la carpeta padre, pero los ficheros `.gitignore` estaban dispersos en subcarpetas. Esto causaba que las reglas no se aplicaran correctamente desde la raíz del repositorio.

## Estructura de directorios antes del fix

```
ApartmentPredictor_Albert_v1\          ← raíz del Git
├── .git\
├── .gitignore                         ← VACÍO, no hacía nada
└── ApartmentPredictor\
    ├── .gitignore                     ← tenía contenido pero mal ubicado
    │                                     además ignoraba maven-wrapper.properties
    └── .idea\
        └── .gitignore                 ← VACÍO, no hacía nada
```

## Problemas identificados

**Problema 1:** El único `.gitignore` con contenido estaba en la subcarpeta `ApartmentPredictor`, no en la raíz del Git. Las reglas solo aplicaban a partir de esa subcarpeta.

**Problema 2:** El `.gitignore` de la subcarpeta tenía esta línea:
```
/.mvn/wrapper/maven-wrapper.properties
```
Esto ignoraba el fichero `maven-wrapper.properties` que necesita IntelliJ para importar el proyecto Maven correctamente. Si alguien clonaba el repo, IntelliJ no podía arrancar el proyecto.

**Problema 3:** Los dos `.gitignore` vacíos (raíz y `.idea`) no servían para nada y generaban confusión.

## Solución paso a paso

### Paso 1: Localizar todos los .gitignore del repositorio

Desde la raíz del Git (`ApartmentPredictor_Albert_v1`):

```bash
find . -name ".gitignore" -not -path "*/.git/*" -exec echo "=== {} ===" \; -exec cat {} \;
```

### Paso 2: Crear el nuevo .gitignore unificado en la raíz

```bash
cat > .gitignore << 'EOF'
# ===========================
# MAVEN
# ===========================
target/
.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/
!**/src/main/**/build/
!**/src/test/**/build/

# ===========================
# INTELLIJ IDEA
# ===========================
.idea/
*.iws
*.iml
*.ipr

# ===========================
# VSCODE
# ===========================
.vscode/

# ===========================
# SPRING BOOT
# ===========================
HELP.md
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

# ===========================
# NETBEANS
# ===========================
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
EOF
```

### Paso 3: Eliminar los .gitignore que ya no se necesitan

```bash
rm ApartmentPredictor/.gitignore
rm ApartmentPredictor/.idea/.gitignore
```

### Paso 4: Hacer commit de los cambios

```bash
git add .
git commit -m "fix: consolidate .gitignore in root folder"
```

### Paso 5: Verificar que solo queda un .gitignore

```bash
find . -name ".gitignore" -not -path "*/.git/*" -exec echo "=== {} ===" \; -exec cat {} \;
```

Resultado esperado — solo un fichero en la raíz con todo el contenido:
```
=== ./.gitignore ===
# ===========================
# MAVEN
# ...
```

## Estructura de directorios después del fix

```
ApartmentPredictor_Albert_v1\          ← raíz del Git
├── .git\
├── .gitignore                         ← UN SOLO fichero aquí con todo
└── ApartmentPredictor\
    ├── src\
    ├── pom.xml
    └── ...                            ← sin .gitignore aquí
```

## Qué ignora cada sección del .gitignore

| Sección | Qué ignora | Por qué |
|---|---|---|
| `target/` | Carpeta de compilación de Maven | Ficheros generados, no van al repo |
| `.mvn/wrapper/maven-wrapper.jar` | El JAR del Maven wrapper | Binario pesado, se descarga automáticamente |
| `!**/src/**/target/` | Excepción: carpetas target dentro de src | Para no ignorar recursos dentro de src |
| `.idea/` | Configuración de IntelliJ | Específica de cada desarrollador |
| `*.iws *.iml *.ipr` | Ficheros de proyecto IntelliJ | Específicos de cada máquina |
| `.vscode/` | Configuración de VSCode | Específica de cada desarrollador |
| `HELP.md` | Fichero de ayuda de Spring Initializr | No aporta valor al repo |
| `.classpath .project .settings` | Ficheros de Eclipse/STS | Específicos del IDE |
| `build/` | Carpeta de compilación de Gradle | Ficheros generados |

## Lecciones aprendidas

- El `.gitignore` siempre debe estar en la **raíz del repositorio Git**, no en subcarpetas, para que las reglas apliquen a todo el proyecto.
- Tener varios `.gitignore` dispersos genera confusión y reglas contradictorias.
- El fichero `.mvn/wrapper/maven-wrapper.properties` **nunca debe ignorarse** — es necesario para que IntelliJ y otros desarrolladores puedan importar el proyecto Maven correctamente.
- Para trabajar con IntelliJ y VSCode en el mismo proyecto basta con una sección para cada uno en un único `.gitignore`.

---
