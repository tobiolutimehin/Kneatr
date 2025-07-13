// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Android Application Plugin
    alias(libs.plugins.android.application) apply false

    // Kotlin Plugins
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false

    // Dependency Injection (Hilt)
    alias(libs.plugins.hilt.android) apply false

    // Kotlin Symbol Processing (KSP)
    alias(libs.plugins.ksp) apply false
}
