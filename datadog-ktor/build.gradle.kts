plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.maven.publish)
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    /* common
     * |-- js
     * |-- macos
     * '-- mobile
     *     |-- ios
     *     '-- jvm
     */

    iosArm64()
    iosSimulatorArm64()
    js().browser()
    jvm()
    macosArm64()
    macosX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.datadog)
                api(libs.ktor.client.core)
                implementation(libs.datetime)
                implementation(libs.ktor.client.content)
                implementation(libs.result)
                implementation(libs.serialization.core)
                implementation(libs.serialization.json)
            }
        }
    }
}
