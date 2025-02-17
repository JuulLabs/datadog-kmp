package com.juul.datadog.ktor

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

public fun <T : HttpClientEngineConfig> ktorDatadogLogger(
    configuration: Configuration,
    scope: CoroutineScope,
    engine: HttpClientEngineFactory<T>,
    config: HttpClientConfig<T>.() -> Unit = {},
): RestLogger = KtorLogger(
    scope,
    DatadogLogSubmission(
        HttpClient(engine) {
            apply(config)

            install(ContentNegotiation) {
                json(Json)
            }

            defaultRequest {
                host = configuration.rest.site.logHost
                header("DD-API-KEY", configuration.rest.apiKey)
                contentType(ContentType.Application.Json)
                url { this.protocol = URLProtocol.HTTPS }
            }
        },
    ),
    configuration.log,
)
