enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "datadog-kmp"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

includeBuild("sample")

include(
    ":bundled",
    ":datadog",
    ":datadog-ktor",
)
