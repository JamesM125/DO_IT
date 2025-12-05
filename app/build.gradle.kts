import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mimi.do_it"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mimi.do_it"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
        implementation("androidx.appcompat:appcompat:1.7.1")
        implementation("com.google.android.material:material:1.13.0")
        implementation("androidx.activity:activity:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.2.1")

        implementation("androidx.recyclerview:recyclerview:1.4.0")
        implementation("androidx.cardview:cardview:1.0.0")
}