enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "datadog-kmp"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

include(
    ":bundled",
    ":datadog",
    ":ktor",
)
