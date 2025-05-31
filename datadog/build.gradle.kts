plugins {
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

    applyDefaultHierarchyTemplate()

    sourceSets {
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }
        jvmMain.get().dependsOn(mobileMain)
        iosMain.get().dependsOn(mobileMain)
    }
}
