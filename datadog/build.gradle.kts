plugins {
    // Android plugin must be before multiplatform plugin until https://youtrack.jetbrains.com/issue/KT-34038 is fixed.
    alias(libs.plugins.android.library)
    kotlin("multiplatform")
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.maven.publish)
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    /* common
     * |-- js
     * |-- mobile
     * |   |-- android
     * |   '-- ios
     * '-- macos
     */

    androidTarget().publishAllLibraryVariants()
    iosArm64()
    iosSimulatorArm64()
    js().browser()
    macosArm64()
    macosX64()

    sourceSets {
        val commonMain by getting

        val mobileMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(mobileMain)
        }

        val iosMain by creating {
            dependsOn(mobileMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val macosMain by creating {
            dependsOn(commonMain)
        }

        val macosArm64Main by getting {
            dependsOn(macosMain)
        }

        val macosX64Main by getting {
            dependsOn(macosMain)
        }
    }
}

android {
    compileSdk = libs.versions.android.compile.get().toInt()
    defaultConfig.minSdk = libs.versions.android.min.get().toInt()
    namespace = "com.juul.datadog"
    lint {
        abortOnError = true
        warningsAsErrors = true
    }
}
