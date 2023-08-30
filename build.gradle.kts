buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.api)
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
