# 📚 Guía de Comandos Git — Explicación Paso a Paso

---

## 1. Clonar el repositorio y entrar en él

```bash
git clone https://github.com/alexpjava/ApartmentPredictor_Albert_v1.git
cd ApartmentPredictor_Albert_v1
```

- **`git clone <url>`** → Descarga una copia completa del repositorio remoto (tu fork de GitHub) en tu máquina local. Crea una carpeta con el nombre del repositorio.
- **`cd ApartmentPredictor_Albert_v1`** → Entra dentro de la carpeta recién creada para trabajar desde ahí.

---

## 2. Retroceder a un commit concreto

```bash
git reset --hard d2da3af
```

- **`git reset --hard <hash>`** → Mueve el puntero de tu rama al commit indicado (`d2da3af`) y **descarta todos los cambios posteriores**, tanto en el staging area como en los archivos. Es un reset total: el repositorio queda exactamente como estaba en ese commit.

> ⚠️ **Cuidado:** cualquier cambio no guardado se pierde de forma permanente.

---

## 3. Forzar la subida al remoto

```bash
git push origin master --force
```

- **`git push origin master`** → Sube los cambios de tu rama local `master` al repositorio remoto llamado `origin` (tu fork en GitHub).
- **`--force`** → Obliga al remoto a aceptar el push aunque haya commits que quedarían eliminados. Es necesario porque hemos retrocedido en el historial y el remoto tiene commits más recientes que ya no queremos.

> ⚠️ Usa `--force` con precaución: reescribe el historial remoto.

---

## 4. Añadir el repositorio del profesor como `upstream`

```bash
git remote add upstream https://github.com/AlbertProfe/ApartmentPredictor.git
```

- **`git remote add <nombre> <url>`** → Registra un nuevo repositorio remoto con el alias `upstream`. Así Git sabe dónde está el repo original del profesor y podrás descargar sus actualizaciones sin mezclarlas con tu propio fork (`origin`).

---

## 5. Verificar los remotos configurados

```bash
git remote -v
```

- **`git remote -v`** → Lista todos los repositorios remotos que tiene configurados tu proyecto local, mostrando su nombre y URL (tanto para fetch como para push). Deberías ver dos entradas: `origin` (tu fork) y `upstream` (repo del profesor).

---

## 6. Crear una nueva rama y situarte en ella

```bash
git checkout -b feature/H2inMemory
```

- **`git checkout -b <nombre-rama>`** → Crea una nueva rama con el nombre indicado **y se mueve a ella** en un solo paso. La rama se crea a partir del commit actual (el que fijamos con `reset --hard`).

---

## 7. Subir la nueva rama a GitHub

```bash
git push -u origin feature/H2inMemory
```

- Publica la rama `feature/H2inMemory` en tu fork de GitHub (`origin`). A partir de aquí, la rama existe tanto en local como en remoto.

- El flag -u (de --set-upstream) vincula tu rama local con la remota. Después de esto VS Code ya no te lo pedirá.

---

## 8. Asegurarte de estar en tu rama de trabajo

```bash
git checkout feature/H2inMemory
```

- **`git checkout <rama>`** → Cambia a la rama indicada. Útil para confirmar que estás trabajando en el lugar correcto antes de hacer cambios.

---

## 9. Guardar tus cambios (add + commit + push)

```bash
git add .
git commit -m "feat: descripción de lo que hiciste"
git push origin feature/H2inMemory
```

- **`git add .`** → Añade **todos** los archivos modificados o nuevos al área de staging (los prepara para el commit).
- **`git commit -m "mensaje"`** → Crea un snapshot (instantánea) del estado actual con el mensaje descriptivo. El prefijo `feat:` sigue la convención [Conventional Commits](https://www.conventionalcommits.org/).
- **`git push origin feature/H2inMemory`** → Sube el commit a GitHub en tu rama de trabajo.

---

## 10. Descargar las actualizaciones del profesor

```bash
git fetch upstream
```

- **`git fetch upstream`** → Descarga los nuevos commits del repositorio del profesor (`upstream`) pero **no los fusiona** con tu código. Solo actualiza las referencias remotas localmente.

---

## 11. Sincronizar tu `master` con el del profesor

```bash
git checkout master
git reset --hard upstream/master
```

- **`git checkout master`** → Cambias a tu rama `master` local.
- **`git reset --hard upstream/master`** → Reemplazas completamente tu `master` con el del profesor. Tu rama `master` queda idéntica a la del repo original.

---

## 12. Volver a tu rama de trabajo

```bash
git checkout feature/H2inMemory
```

- Regresas a tu rama de desarrollo para continuar trabajando.

---

## 13. Crear una nueva rama desde un commit específico de `master`

```bash
git status
git log --oneline master -n 15
git checkout -b feature/nuevo-ejercicio HASH
git push origin feature/nuevo-ejercicio
```

- **`git status`** → Comprueba que no hay cambios pendientes en la rama actual (debe mostrar "working tree clean").
- **`git log --oneline master -n 15`** → Muestra el historial de `master` en formato compacto (una línea por commit), limitado a los últimos 15. Aquí localizas el hash del commit desde el que quieres partir.
- **`git checkout -b feature/nuevo-ejercicio HASH`** → Crea una nueva rama empezando exactamente en el commit `HASH` (sustituye `HASH` por el código real del log).
- **`git push origin feature/nuevo-ejercicio`** → Publica la nueva rama en GitHub.

---

## 14. Comandos de inspección y diagnóstico

```bash
# Ver en qué rama estás y su estado respecto al remoto
git branch -vv

# Ver historial gráfico de commits
git log --oneline --graph --decorate -n 20

# Ver todos los remotos configurados
git remote -v

# Ver diferencias entre tu rama y el master del profesor
git diff HEAD upstream/master
```

| Comando | ¿Qué hace? |
|---|---|
| `git branch -vv` | Lista todas las ramas locales con su último commit y si están adelantadas/atrasadas respecto al remoto |
| `git log --oneline --graph --decorate -n 20` | Muestra el historial de commits como un árbol visual, con etiquetas de ramas y tags, limitado a 20 entradas |
| `git remote -v` | Lista los repositorios remotos registrados con sus URLs |
| `git diff HEAD upstream/master` | Muestra qué líneas son distintas entre tu commit actual y el `master` del profesor |

---

## 15. ¿Qué es `origin` y `master`?

```bash
git push origin master --force
```

Son dos conceptos distintos que van juntos en el comando:

### `origin`

Es el **nombre (alias) del repositorio remoto** al que quieres subir el código. Cuando haces `git clone`, Git automáticamente llama `origin` al repositorio del que clonaste. Es simplemente un apodo para no tener que escribir la URL entera cada vez.

```bash
# Estas dos líneas hacen lo mismo:
git push origin master
git push https://github.com/alexpjava/ApartmentPredictor_Albert_v1.git master
```

Por eso en este proyecto tienes **dos remotos**:
- `origin` → tu fork (GitHub tuyo)
- `upstream` → el repo del profesor

### `master`

Es el **nombre de la rama** que quieres subir. Le estás diciendo a Git: *"sube la rama llamada `master` al remoto llamado `origin`"*. Podría ser cualquier rama:

```bash
git push origin master               # sube la rama master
git push origin feature/H2inMemory  # sube esa otra rama
```

La estructura general del comando es siempre:

```
git push  <remoto>  <rama>
```

> *"Sube (`push`) al remoto `origin` (mi fork en GitHub) la rama `master`, y hazlo a la fuerza aunque el historial no coincida (`--force`)."*

---

## 16. Moverse a un commit antiguo sin `reset`

Si quieres crear una rama desde un commit antiguo **sin tocar ni destruir** tu rama actual, usa `git checkout` directamente con el hash:

```bash
# 1. Ver el historial para localizar el commit
git log --oneline -n 15

# 2. Moverte a ese commit Y crear la rama en un solo paso
git checkout -b feature/nueva-rama abc1234
```

Donde `abc1234` es el hash del commit antiguo.

### ¿Qué diferencia hay con `reset`?

| | `git checkout -b rama HASH` | `git reset --hard HASH` |
|---|---|---|
| Tu rama actual | **no se toca** | **retrocede** al commit |
| Historial | intacto | los commits posteriores desaparecen |
| Seguro | ✅ sí | ⚠️ destructivo |

### Ejemplo práctico

Si estás en `master` y quieres crear una rama desde un commit antiguo sin tocar nada:

```bash
git log --oneline master -n 15          # miras el historial
git checkout -b feature/nuevo-ejercicio d2da3af   # creas la rama desde ese punto
git push origin feature/nuevo-ejercicio  # la subes a GitHub
```

Tu `master` queda **intacto**. La nueva rama simplemente empieza desde ese commit antiguo.

---

## 🗺️ Resumen del flujo de trabajo

```
[upstream/profesor]
       |
   git fetch upstream
       |
   git reset --hard upstream/master  →  [tu master local]
                                               |
                                   git checkout -b feature/xxx HASH
                                               |
                                        haz tus cambios
                                               |
                                   git add . && git commit
                                               |
                                   git push origin feature/xxx
                                               |
                                        [tu fork en GitHub]
```