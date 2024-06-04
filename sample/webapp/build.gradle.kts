plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlinter)
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        jsMain.dependencies {
            implementation(projects.library)
        }
    }
}
