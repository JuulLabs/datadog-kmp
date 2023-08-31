enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {
            when {
                requested.id.namespace == "com.android" ->
                    useModule("com.android.tools.build:gradle:${requested.version}")

                requested.id.id == "binary-compatibility-validator" ->
                    useModule("org.jetbrains.kotlinx:binary-compatibility-validator:${requested.version}")
            }
        }
    }
}

include(
    ":android",
    ":ios",
    ":library",
    ":webapp",
)
