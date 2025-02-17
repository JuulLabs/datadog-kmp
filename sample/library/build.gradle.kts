import java.util.Properties
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    // Android plugin must be before multiplatform plugin until https://youtrack.jetbrains.com/issue/KT-34038 is fixed.
    alias(libs.plugins.android.library)
    kotlin("multiplatform")
    alias(libs.plugins.swiftpackage)
    alias(libs.plugins.kotlinter)
    `maven-publish`
}

fun String.execute(): String =
    Runtime.getRuntime()
        .exec(this)
        .inputStream
        .bufferedReader()
        .readText()

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    /* common
     * |-- bundled
     * |   |-- android
     * |   '-- js
     * '-- ios
     */

    androidTarget().publishAllLibraryVariants()
    iosArm64()
    iosSimulatorArm64()
    js().browser()

    applyDefaultHierarchyTemplate()

    val xcf = XCFramework()
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "SampleLibrary"

            binaryOption("bundleId", "com.juul.datadog.sample")
            binaryOption(
                "bundleShortVersionString",
                (project.version as? String)
                    ?.takeUnless { it == Project.DEFAULT_VERSION }
                    ?: "git describe --tags".execute().trim().trimStart('v'), // e.g. "1.2.3-rc1-41-g7da5c7c12"
            )
            binaryOption(
                "bundleVersion",
                project.properties["bundleVersion"] as? String
                    ?: "git rev-list --count HEAD".execute().trim() // Git commit count, e.g. "614"
            )

            export(libs.datadog.kmp)
            export(libs.nserrorkt)
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.datadog.kmp)
            }
        }

        val bundledMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(libs.datadog.kmp.bundled)
            }
        }

        getByName("androidMain") {
            dependsOn(bundledMain)
            kotlin.srcDir(layout.buildDirectory.file("generated/sources/datadog/$name/kotlin"))
        }

        getByName("iosMain") {
            kotlin.srcDir(layout.buildDirectory.file("generated/sources/datadog/$name/kotlin"))
            dependencies {
                api(libs.nserrorkt)
            }
        }

        getByName("jsMain") {
            dependsOn(bundledMain)
            kotlin.srcDir(layout.buildDirectory.file("generated/sources/datadog/$name/kotlin"))
        }
    }
}

multiplatformSwiftPackage {
    packageName("SampleLibrary")
    swiftToolsVersion("5.7")
    targetPlatforms {
        iOS { v("14") }
    }
    outputDirectory(layout.buildDirectory.file("swiftpackage").get().asFile)
    zipFileName("sample-library")
}

android {
    compileSdk = libs.versions.android.compile.get().toInt()
    defaultConfig.minSdk = libs.versions.android.min.get().toInt()
    namespace = "com.juul.datadog.sample"
    lint {
        abortOnError = true
        warningsAsErrors = true
    }
}

tasks.register("datadogClientTokens") {
    description = "Writes Datadog client tokens to Kotlin source code for use in the project"
    group = "Build"

    doFirst {
        val properties = Properties().apply {
            load(file("$projectDir/local.properties").inputStream())
        }

        listOf("android", "ios", "js",).forEach { target ->
            val property = "datadog.clientToken.$target"
            val token = properties.getOrElse(property) { error("Missing $property in $projectDir/local.properties") }
            val path = layout.buildDirectory.file("generated/sources/datadog/${target}Main/kotlin").get().asFile
            val sourceFile = file("$path/DatadogClientToken.kt")
            path.mkdirs()
            sourceFile.createNewFile()
            sourceFile.writeText(
                """
                package com.juul.datadog.sample
                
                internal actual val DATADOG_CLIENT_TOKEN: String = "$token"
                
                """.trimIndent()
            )
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
    dependsOn("datadogClientTokens")
    shouldRunAfter("clean")
}

tasks.withType<Sync> {
    dependsOn("datadogClientTokens")
}

tasks.named("preBuild") {
    dependsOn("datadogClientTokens")
    shouldRunAfter("clean")
}
