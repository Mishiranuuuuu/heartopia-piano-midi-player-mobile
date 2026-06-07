// Top-level build file.
// Declares all build plugins used across modules, but applies them only in
// each module's own build.gradle.kts (apply false = register, don't apply).
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
