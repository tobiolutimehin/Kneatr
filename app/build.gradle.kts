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

        testInstrumentationRunner = "com.hollowvyn.kneatr.CustomTestRunner"
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
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
    implementation(libs.androidx.compose.adaptive.navigation3.android)

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.material3.navigation3)

    // Lifecycle + ViewModel + Navigation
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Kotlin
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlinx.datetime)
    testImplementation(kotlin("test"))

    // DI / Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)

    // UI Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.hilt.compiler)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Google
    implementation(libs.google.libphonenumber)
}
