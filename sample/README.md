# Datadog KMP Sample

Suite of sample applications demonstrating usage of the [Datadog KMP] (Kotlin multiplatform) library.

The sample apps depend on a common library that provides Datadog client tokens, the tokens should be
configured by adding them to `library/local.properties` file as follows:

```
datadog.clientToken.android=YOUR_ANDROID_CLIENT_TOKEN
datadog.clientToken.ios=YOUR_IOS_CLIENT_TOKEN
datadog.clientToken.js=YOUR_JAVASCRIPT_CLIENT_TOKEN
```

## Android

The Android sample app can be built and installed via [Android Studio], or via command line by
executing:

```shell
./gradlew installDebug
```

## iOS

The iOS sample app demonstrates using the [Datadog KMP] library by means of a Framework that exposes
[Datadog KMP] protocols that are implemented in the app layer using official Datadog dependencies
pulled directly into the iOS app.

Due to limitations with Kotlin Multiplatform, applications can reasonably only have a single Kotlin
dependency — the `library` module is used to produce the Framework used in the sample app, and shows
how [Datadog KMP] can be integrated into an existing library.

Kotlin/Native does not provide official support for SPM (Swift package manager), as a result, the
sample app pulls in Datadog SPM dependency directly and implements the necessary [Datadog KMP]
protocols.

The sample app can be opened in Xcode by executing:

```shell
./gradlew openXcode
```

_Requires `xcodegen` installed via Homebrew (`xcodegen` located at `/opt/homebrew/bin/xcodegen`, or
modify `ios/build.gradle.kts` `generateXcodeProject` task)._

## JavaScript

To build and launch the demo within a browser window, run:

```shell
./gradlew jsBrowserRun
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


[Datadog KMP]: https://github.com/JuulLabs/datadog-kmp
[Android Studio]: https://developer.android.com/studio
