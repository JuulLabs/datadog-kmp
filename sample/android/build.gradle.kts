plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.compose)
}

android {
    compileSdk = libs.versions.android.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.min.get().toInt()
        targetSdk = libs.versions.android.target.get().toInt()
    }
    namespace = "com.juul.datadog.sample.android"
    lint {
        abortOnError = true
        warningsAsErrors = true
        disable += listOf("AndroidGradlePluginVersion", "GradleDependency", "MissingApplicationIcon")
    }
    buildFeatures.compose = true
}

dependencies {
    implementation(projects.library)
    implementation(libs.bundles.compose)
}
