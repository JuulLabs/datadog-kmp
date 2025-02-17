# DEPRECATED

> [!IMPORTANT]
> This library has been deprecated in favor of [official support](https://github.com/DataDog/dd-sdk-kotlin-multiplatform).

<details>
<summary>Previous README</summary>

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

# License

```
Copyright 2023 JUUL Labs, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
</details>


[Datadog]: https://www.datadoghq.com/
[Datadog KMP sample app]: https://github.com/JuulLabs/datadog-kmp-sample
