buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinter) apply false
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
