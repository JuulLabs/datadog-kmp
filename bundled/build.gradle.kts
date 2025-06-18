plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.maven.publish)
    kotlin("multiplatform")
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    // Platforms supported by https://github.com/DataDog/dd-sdk-kotlin-multiplatform:
    androidTarget().publishAllLibraryVariants()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // Platforms not supported by https://github.com/DataDog/dd-sdk-kotlin-multiplatform:
    js().browser()

    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }

        commonMain.dependencies {
            api(projects.datadog)
        }

        commonTest.dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        val datadogMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                api(libs.datadog.multiplatform.logs)
            }
        }

        androidMain.get().dependsOn(datadogMain)
        androidMain.dependencies {
            implementation(libs.androidx.startup)
        }

        iosMain.get().dependsOn(datadogMain)
        iosMain.dependencies {
            implementation(libs.nserrorkt)
        }

        jsMain.dependencies {
            implementation(npm("@datadog/browser-logs", libs.versions.datadog.npm.get()))
        }

        jsTest.dependencies {
            implementation(kotlin("test-js"))
        }
    }
}

android {
    compileSdk = libs.versions.android.compile.get().toInt()
    defaultConfig.minSdk = libs.versions.android.min.get().toInt()

    namespace = "com.juul.datadog.bundled"

    lint {
        abortOnError = true
        warningsAsErrors = true
        disable += "AndroidGradlePluginVersion"
        disable += "GradleDependency"
    }
}
