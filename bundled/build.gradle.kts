plugins {
    // Android plugin must be before multiplatform plugin until https://youtrack.jetbrains.com/issue/KT-34038 is fixed.
    alias(libs.plugins.android.library)
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.maven.publish)
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    androidTarget().publishAllLibraryVariants()
    iosArm64()
    iosSimulatorArm64()
    js().browser()

    cocoapods {
        ios.deploymentTarget = "11.0"

        summary = "Datadog KMP"
        homepage = "https://www.datadoghq.com"
        version = (project.version as String)
            .takeUnless { it == Project.DEFAULT_VERSION }
            ?: "0.0.1-dev"

        pod("DatadogObjc") {
            version = "~> ${libs.versions.datadog.ios.get()}"
        }
        pod("DatadogCore") {
            version = "~> ${libs.versions.datadog.ios.get()}"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.datadog)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.datadog.logs)
                implementation(libs.androidx.startup)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("@datadog/browser-logs", libs.versions.datadog.npm.get()))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.nserrorkt)
            }
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
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
    }
}

// Workaround for missing Datadog imports for Datadog 1.x, or missing symbols for Datadog 2.x.
// https://youtrack.jetbrains.com/issue/KT-44724
tasks.named<org.jetbrains.kotlin.gradle.tasks.DefFileTask>("generateDefDatadogObjc").configure {
    doLast {
        outputFile.writeText("""
            language = Objective-C
            modules = DatadogInternal DatadogObjc
        """)
    }
}
