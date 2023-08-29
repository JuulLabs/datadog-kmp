# Datadog KMP (Kotlin Multiplatform)

Datadog KMP provides a unified [Datadog] API for use in Kotlin multiplatform projects.

Usage is demonstrated with the [Datadog KMP sample app].

## `datadog`

The `datadog` module provides the core API w/o having transitive dependencies on Datadog libraries.
Useful when it is desired to have Datadog libraries pulled into your application (separate from
Datadog KMP) and provide implementations of the API in your application.

```gradle
dependencies {
    api("com.juul.datadog:datadog:${datadogKmpVersion}")
}
```

## `bundled`

The `bundled` module provides implementations of the `datadog` module and pulls in (transitively)
the Datadog libraries. Useful for applications/libraries that don't yet have Datadog integrated and
want to add a "batteries included" Datadog solution.

```gradle
dependencies {
    api("com.juul.datadog:bundled:${datadogKmpVersion}")
}
```


[Datadog]: https://www.datadoghq.com/
[Datadog KMP sample app]: https://github.com/JuulLabs/datadog-kmp-sample
