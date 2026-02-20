# 🧭 Git Workflow Guide — Fork + Branches per Exercise

> Personal reference guide to manage the professor's project fork without unwanted merges.

---

## 📋 Project Context

| Element | Value |
|---|---|
| **Your fork (origin)** | `https://github.com/alexpjava/ApartmentPredictor_Albert_v1` |
| **Professor's repo (upstream)** | `https://github.com/AlbertProfe/ApartmentPredictor` |
| **Base commit (starting point)** | `d2da3af` |
| **Current working branch** | `feature/H2inMemory` |

---

## 🚀 Initial Setup (from scratch)

### Step 1 — Delete the old fork on GitHub
1. Go to your repository on GitHub
2. `Settings` → scroll to the bottom → **Delete this repository**
3. Confirm by typing the repo name

### Step 2 — Create a new Fork
1. Go to the professor's repository on GitHub
2. Click **Fork** (top right) → make sure *"Copy the main branch only"* is checked
3. Click **Create fork**

### Step 3 — Clone and travel back to the base commit

```bash
# Clone your fork
git clone https://github.com/alexpjava/ApartmentPredictor_Albert_v1.git
cd ApartmentPredictor_Albert_v1

# Go back to the desired starting commit
git reset --hard d2da3af

# Force your remote GitHub to match your local state
git push origin master --force
```

### Step 4 — Add the professor as an upstream source

```bash
git remote add upstream https://github.com/AlbertProfe/ApartmentPredictor.git

# Verify you have both remotes
git remote -v
```

You should see:
```
origin    https://github.com/alexpjava/ApartmentPredictor_Albert_v1 (fetch/push)
upstream  https://github.com/AlbertProfe/ApartmentPredictor (fetch/push)
```

---

## 🌿 Creating a Working Branch (per exercise)

**⚠️ Golden rule: never work directly on `master`.**

```bash
# Create the branch from the current commit and switch to it
git checkout -b feature/H2inMemory

# Push the branch to your GitHub
git push origin feature/H2inMemory
```

---

## 📅 Daily Workflow

```bash
# 1. Make sure you are on your branch
git checkout feature/H2inMemory

# 2. Make your changes... then:
git add .
git commit -m "feat: description of what you did"

# 3. Push your changes
git push origin feature/H2inMemory
```

---

## 🔭 Checking the Professor's Progress (without affecting your code)

```bash
# Download the professor's latest changes
git fetch upstream

# Switch to master and sync it with the professor
git checkout master
git reset --hard upstream/master

# Go back to your working branch
git checkout feature/H2inMemory
```

> `master` acts as a mirror of the professor's repo. Your `feature/...` branches remain untouched.

---

## ⏩ Starting a New Exercise (X commits ahead)

When you finish an exercise and the next one starts from a more advanced point in the professor's history:

```bash
# 1. Make sure everything is committed in your current branch
git status  # should be clean

# 2. Check the master history to find the target commit
git log --oneline master -n 15

# 3. Create the new branch from that commit (replace HASH with the actual code)
git checkout -b feature/new-exercise HASH

# 4. Push it to GitHub
git push origin feature/new-exercise
```

For the next exercise: find the commit **5 positions ahead** of `d2da3af` in the `master` log.

---

## 🗺️ Branch Structure

```
master               ──────────────────────────────────► (professor's mirror, do not touch)
                          │              │
                      d2da3af       d2da3af+5
                          │              │
feature/H2inMemory  ──────►        feature/next-exercise ──────►
```

---

## 🛠️ Useful Diagnostic Commands

```bash
# See which branch you are on and its tracking status
git branch -vv

# View graphical commit history
git log --oneline --graph --decorate -n 20

# List all configured remotes
git remote -v

# See the difference between your branch and the professor's master
git diff HEAD upstream/master
```

---

## ⚠️ Common Mistakes to Avoid

| ❌ Don't do this | ✅ Do this instead |
|---|---|
| Using the **Sync Fork** button on GitHub | `git fetch upstream` + `git reset --hard upstream/master` on master |
| Working directly on `master` | Always create a `feature/...` branch |
| `git push` from master with new commits | Keep master as a read-only mirror of the professor |
| `git merge` to update your branch | Use `git cherry-pick HASH` to bring in specific changes from the professor |

---

## 💡 Quick Glossary

- **origin**: your fork on GitHub
- **upstream**: the professor's original repository
- **reset --hard**: moves your branch to an exact point in history, discarding everything after it
- **push --force**: forces GitHub to accept a rewritten history (use with care)
- **cherry-pick**: copies a specific commit from another branch without bringing everything else along
