# 📖 **Side Project Infra Handbook (Android & Software Projects)**

> **Purpose:** A reusable, opinionated guide for efficiently bootstrapping Android mono-module and
> multi-module projects.
> **Focus:** Skip busywork. Enforce best practices. Keep productivity high from Day 1.

---

# 📚 **Table of Contents**

1. [Directory Structure](#1-directory-structure)
2. [Templates](#2-templates)
3. [CI/CD Setup](#3-cicd-setup)
4. [GitHub Apps](#4-github-apps)
5. [Branch Protection](#5-branch-protection)
6. [Issue & PR Labels](#6-issue--pr-labels)
7. [Bulk Issue Generator](#7-bulk-issue-generator)
8. [Lint & Static Analysis](#8-lint--static-analysis)
9. [Why This Template Exists](#9-why-this-template-exists)
10. [Next Steps for New Projects](#10-next-steps-for-new-projects)

---

# 1️⃣ **Directory Structure**

## Mono-Module Layout

```
/app
/docs
.github/
    workflows/
    ISSUE_TEMPLATE/
.gitignore
README.md
LICENSE
release-drafter.yml
```

## Multi-Module Layout

```
/app
/core
/feature-[name]
/docs
.github/
    workflows/
    ISSUE_TEMPLATE/
.gitignore
README.md
LICENSE
release-drafter.yml
/build.gradle.kts
/settings.gradle.kts
```

---

# 2️⃣ **Templates**

## `.github/ISSUE_TEMPLATE/`

| File                 | Purpose              |
|----------------------|----------------------|
| `bug_report.md`      | Standard bug reports |
| `feature_request.md` | Feature proposals    |
| `project_task.md`    | Infra / maintenance  |

## `docs/`

| File                        | Purpose                         |
|-----------------------------|---------------------------------|
| `ARCHITECTURE_DECISIONS.md` | Document architecture decisions |
| `DESIGN_GUIDELINES.md`      | Design system / UI principles   |
| `MVP_SCOPE.md`              | MVP feature scope, constraints  |

---

# 3️⃣ **CI/CD Setup**

### Android CI (Baseline)

```yaml
name: Android CI
on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: gradle/wrapper-validation-action@v1
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: 17
      - run: chmod +x gradlew
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew testDebugUnitTest
      - run: ./gradlew assembleDebug
```

### Release Drafter

Auto-changelog support via `.github/workflows/release-drafter.yml`.

---

# 4️⃣ **GitHub Apps**

| App                 | Purpose              |
|---------------------|----------------------|
| **CodeQL**          | Security analysis    |
| **Renovate**        | Dependency updates   |
| **Mend**            | Security checks      |
| **Release Drafter** | Changelog automation |

---

# 5️⃣ **Branch Protection**

✅ PR reviews required
✅ Status checks:

* Build
* Unit tests
* Gradle wrapper validation

✅ Dismiss stale approvals on new commits

---

# 6️⃣ **Issue & PR Labels**

| Label     | Purpose      |
|-----------|--------------|
| `phase-1` | MVP tasks    |
| `phase-2` | Enhancements |
| `infra`   | CI/CD, setup |
| `bug`     | Bugs         |
| `feature` | Features     |
| `chore`   | Maintenance  |

---

# 7️⃣ **Bulk Issue Generator**

## `generate_tasks.py`

Python CLI tool for fast issue creation.

```bash
python3 generate_tasks.py --dry-run
```

✅ JSON input\
✅ CSV output\
✅ GitHub CLI integration\
✅ Dry-run preview

---

# 8️⃣ **Lint & Static Analysis**

✅ Handled in **Android Studio Plugins** (ktlint / detekt)
❌ No CI integration for now

---

# 9️⃣ **Why This Template Exists**

| Benefit    | Why It Matters          |
|------------|-------------------------|
| Fast Start | Skip infra busywork     |
| Clean      | Consistent repo hygiene |
| Scalable   | Mono / Multi-module     |
| Repeatable | Share across projects   |
| Automated  | CI/CD, issue generation |

---

# 🔟 **Next Steps for New Projects**

1️⃣ Fork this repo

2️⃣ Customize `.github/workflows` as needed

3️⃣ Update issue templates if required

4️⃣ Use `generate_tasks.py` to bulk-generate issues

5️⃣ Start building 🚀

---