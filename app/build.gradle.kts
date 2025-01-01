plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "jie.wen.entaintestapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "jie.wen.entaintestapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources  = true
    }
}

kapt {
    javacOptions {
        // These options are normally set automatically via the Hilt Gradle plugin, but we
        // set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}

dependencies {
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)
    implementation(libs.hilt.android)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    // Kotlin
    implementation(libs.androidx.fragment.ktx)
    // Compose
    implementation(libs.androidx.fragment.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.converter.moshi)
    implementation(libs.converter.scalars)

    implementation(libs.androidx.lifecycle.runtime.compose)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockk)
    testImplementation(libs.mockito.kotlin.kt1.x)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Testing Fragments in Isolation
    debugImplementation(libs.androidx.fragment.testing)
}