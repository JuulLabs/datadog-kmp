plugins {
    alias(libs.plugins.atomicfu)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.maven.publish)
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js().browser()
    jvm()
    macosArm64()
    macosX64()
    wasmJs().browser()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            api(libs.serialization.json)
        }
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }
        jvmMain.get().dependsOn(mobileMain)
        jvmMain.dependencies {
            implementation(libs.atomicfu)
        }
        iosMain.get().dependsOn(mobileMain)
    }
}
