package com.juul.datadog.ktor

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

public fun ktorDatadogLogger(
    configuration: DatadogKtorConfiguration,
    http: HttpClient,
    scope: CoroutineScope,
): RestLogger = KtorLogger(
    scope,
    DatadogLogSubmission(http, configuration.rest),
    configuration.log,
)
