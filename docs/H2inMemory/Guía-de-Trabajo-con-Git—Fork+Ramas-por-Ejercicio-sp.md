# 🧭 Guía de Trabajo con Git — Fork + Ramas por Ejercicio

> Recordatorio personal para gestionar el fork del proyecto del profesor sin merges indeseados.

---

## 📋 Contexto del Proyecto

| Elemento | Valor |
|---|---|
| **Tu fork (origin)** | `https://github.com/alexpjava/ApartmentPredictor_Albert_v1` |
| **Repo del profesor (upstream)** | `https://github.com/AlbertProfe/ApartmentPredictor` |
| **Commit base (punto de partida)** | `d2da3af` |
| **Rama ejercicio actual** | `feature/H2inMemory` |

---

## 🚀 Configuración Inicial (desde cero)

### Paso 1 — Borrar el fork antiguo en GitHub
1. Entra en tu repositorio en GitHub
2. `Settings` → scroll hasta abajo → **Delete this repository**
3. Confirma escribiendo el nombre del repo

### Paso 2 — Crear nuevo Fork
1. Ve al repo del profesor en GitHub
2. Clic en **Fork** → asegúrate de marcar *"Copy the main branch only"*
3. Clic en **Create fork**

### Paso 3 — Clonar y viajar al commit base

```bash
# Clonar tu fork
git clone https://github.com/alexpjava/ApartmentPredictor_Albert_v1.git
cd ApartmentPredictor_Albert_v1

# Retroceder al commit de partida
git reset --hard d2da3af

# Forzar que tu GitHub remoto también quede en ese punto
git push origin master --force
```

### Paso 4 — Añadir al profesor como fuente (upstream)

```bash
git remote add upstream https://github.com/AlbertProfe/ApartmentPredictor.git

# Verificar que tienes los dos remotos
git remote -v
```

Deberías ver:
```
origin    https://github.com/alexpjava/ApartmentPredictor_Albert_v1 (fetch/push)
upstream  https://github.com/AlbertProfe/ApartmentPredictor (fetch/push)
```

---

## 🌿 Crear una Rama de Trabajo (por ejercicio)

**⚠️ Regla de oro: nunca trabajes directamente en `master`.**

```bash
# Crear la rama desde el commit deseado y situarte en ella
git checkout -b feature/H2inMemory

# Subir la rama a tu GitHub
git push origin feature/H2inMemory
```

---

## 📅 Flujo de Trabajo Diario

```bash
# 1. Asegúrate de estar en tu rama
git checkout feature/H2inMemory

# 2. Haz tus cambios... luego:
git add .
git commit -m "feat: descripción de lo que hiciste"

# 3. Sube tus cambios
git push origin feature/H2inMemory
```

---

## 🔭 Ver los Avances del Profesor (sin ensuciar tu código)

```bash
# Descargar lo nuevo del profesor
git fetch upstream

# Ir a master y sincronizarlo con el profesor
git checkout master
git reset --hard upstream/master

# Volver a tu rama de trabajo
git checkout feature/H2inMemory
```

> `master` actúa como espejo del profesor. Tus ramas `feature/...` permanecen intactas.

---

## ⏩ Empezar un Nuevo Ejercicio (X commits adelante)

Cuando termines un ejercicio y el siguiente parte de un punto más avanzado del historial del profesor:

```bash
# 1. Asegúrate de tener todo commitado en tu rama actual
git status  # debe estar limpio

# 2. Mira el historial de master para localizar el commit destino
git log --oneline master -n 15

# 3. Crea la nueva rama desde ese commit (sustituye HASH por el código real)
git checkout -b feature/nuevo-ejercicio HASH

# 4. Súbela a GitHub
git push origin feature/nuevo-ejercicio
```

Para el próximo ejercicio: busca el commit que está **5 posiciones** por delante de `d2da3af` en el log de `master`.

---

## 🗺️ Estructura de Ramas

```
master               ──────────────────────────────────► (espejo del profesor, no tocar)
                          │              │
                      d2da3af       d2da3af+5
                          │              │
feature/H2inMemory  ──────►        feature/siguiente-ejercicio ──────►
```

---

## 🛠️ Comandos de Diagnóstico Útiles

```bash
# Ver en qué rama estás y su estado
git branch -vv

# Ver historial gráfico
git log --oneline --graph --decorate -n 20

# Ver todos los remotos configurados
git remote -v

# Ver diferencia entre tu rama y master del profesor
git diff HEAD upstream/master
```

---

## ⚠️ Errores Comunes a Evitar

| ❌ No hagas esto | ✅ Haz esto en su lugar |
|---|---|
| Usar el botón **Sync Fork** en GitHub | `git fetch upstream` + `git reset --hard upstream/master` en master |
| Trabajar directamente en `master` | Crear siempre una rama `feature/...` |
| `git push` desde master con commits nuevos | Deja master solo como espejo del profesor |
| `git merge` para actualizar tu rama | Usa `git cherry-pick HASH` si quieres algo puntual del profesor |

---

## 💡 Glosario Rápido

- **origin**: tu fork en GitHub
- **upstream**: el repositorio original del profesor
- **reset --hard**: mueve tu rama a un punto exacto del historial, descartando lo posterior
- **push --force**: obliga a GitHub a aceptar un historial reescrito (úsalo con cuidado)
- **cherry-pick**: copia un commit concreto de otra rama sin traerte todo lo demás

```bash
# Clonar tu fork
git clone https://github.com/alexpjava/ApartmentPredictor_Albert_v1.git
cd ApartmentPredictor_Albert_v1

# Retroceder al commit de partida
git reset --hard d2da3af

# Forzar que tu GitHub remoto también quede en ese punto
git push origin master --force

# Añadir al profesor como fuente (upstream)
git remote add upstream https://github.com/AlbertProfe/ApartmentPredictor.git

# Verificar que tienes los dos remotos
git remote -v

# Crear la rama desde el commit deseado y situarte en ella
git checkout -b feature/H2inMemory

# Subir la rama a tu GitHub
git push origin feature/H2inMemory

# 1. Asegúrate de estar en tu rama
git checkout feature/H2inMemory

# 2. Haz tus cambios... luego:
git add .
git commit -m "feat: descripción de lo que hiciste"

# 3. Sube tus cambios
git push origin feature/H2inMemory

# Descargar lo nuevo del profesor
git fetch upstream

# Ir a master y sincronizarlo con el profesor
git checkout master
git reset --hard upstream/master

# Volver a tu rama de trabajo
git checkout feature/H2inMemory

# 1. Asegúrate de tener todo commitado en tu rama actual
git status  # debe estar limpio

# 2. Mira el historial de master para localizar el commit destino
git log --oneline master -n 15

# 3. Crea la nueva rama desde ese commit (sustituye HASH por el código real)
git checkout -b feature/nuevo-ejercicio HASH

# 4. Súbela a GitHub
git push origin feature/nuevo-ejercicio
# Ver en qué rama estás y su estado
git branch -vv

# Ver historial gráfico
git log --oneline --graph --decorate -n 20

# Ver todos los remotos configurados
git remote -v

# Ver diferencia entre tu rama y master del profesor
git diff HEAD upstream/master
```
