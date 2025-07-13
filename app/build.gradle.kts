plugins {
    // Android
    alias(libs.plugins.android.application)
    // Kotlin
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    // Dependency Injection
    alias(libs.plugins.hilt.android)
    // KSP
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.hollowvyn.kneatr"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hollowvyn.kneatr"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    // Compose Adaptive / Canonical Layouts
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.navigationSuite)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.hilt.navigation.compose)

    // Lifecycle + ViewModel + Navigation
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Serialization
    implementation(libs.kotlinx.serialization.core)

    // DI / Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
