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
        val commonMain by getting

        val mobileMain by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
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
