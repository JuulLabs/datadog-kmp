plugins {
    kotlin("multiplatform")
    alias(libs.plugins.atomicfu)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.maven.publish)
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    iosArm64()
    iosSimulatorArm64()
    js().browser()
    jvm()
    macosArm64()
    macosX64()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }

        commonMain.dependencies {
            api(projects.datadog)
            api(libs.ktor.client.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.ktor.client.content)
            implementation(libs.ktor.json)
            implementation(libs.result)
        }
    }
}
