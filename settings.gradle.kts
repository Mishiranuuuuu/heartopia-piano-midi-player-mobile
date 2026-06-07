// ─── Plugin Management ──────────────────────────────────────────────────
// Defines repositories where Gradle looks for build plugins (AGP, Kotlin, etc.).
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

// ─── Dependency Resolution ──────────────────────────────────────────────
// Centralises all dependency repositories here; subproject-level repo declarations are forbidden.
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// ─── Project Structure ──────────────────────────────────────────────────
rootProject.name = "Piano Player"
include(":app")  // The single application module
