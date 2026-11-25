plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.compose)
}

android {
    compileSdk {
        version = release(libs.versions.android.compile.major.get().toInt()) {
            minorApiLevel = libs.versions.android.compile.minor.get().toInt()
        }
    }
    defaultConfig {
        minSdk = libs.versions.sample.android.min.get().toInt()
        targetSdk = libs.versions.android.target.get().toInt()
    }
    namespace = "com.juul.datadog.sample.android"
    lint {
        abortOnError = true
        warningsAsErrors = true
        disable += listOf(
            "AndroidGradlePluginVersion",
            "GradleDependency",
            "MissingApplicationIcon",
            "ObsoleteLintCustomCheck",
        )
    }
    buildFeatures.compose = true
}

dependencies {
    implementation(projects.library)
    implementation(libs.bundles.compose)
}
