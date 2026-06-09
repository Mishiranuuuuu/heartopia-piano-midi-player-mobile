# ─── ProGuard / R8 rules for Piano Auto Clicker ───────────────────────
#
# These rules run only for the release build type (isMinifyEnabled = true).
# They prevent R8 from removing or renaming classes that Android instantiates
# via reflection (e.g. accessibility services declared in the manifest).

# Keep the Accessibility Service — the Android framework instantiates it by
# class name from AndroidManifest.xml. Obfuscating it would cause a crash.
-keep class com.autoclicker.app.service.AutoClickerAccessibilityService { *; }

# Keep FloatingOverlayService and data classes to prevent R8 over-optimization
-keep class com.autoclicker.app.service.FloatingOverlayService { *; }
-keep class com.autoclicker.app.data.ClickConfig { *; }
-keep class com.autoclicker.app.data.LayoutType { *; }

# Suppress harmless warnings from Jetpack Compose internals.
-dontwarn androidx.compose.**
