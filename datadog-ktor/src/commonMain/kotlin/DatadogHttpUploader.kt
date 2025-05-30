package com.juul.datadog.ktor

import com.juul.datadog.Site
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
import kotlinx.serialization.json.Json

public fun <T : HttpClientEngineConfig> datadogHttpUploader(
    configuration: Configuration.Rest,
    engine: HttpClientEngineFactory<T>,
    config: HttpClientConfig<*>.() -> Unit = {},
): RawUploader = RawUploader(
    HttpClient(engine) {
        configure(configuration.site, configuration.apiKey, config)
    },
)

public fun datadogHttpUploader(
    configuration: Configuration.Rest,
    config: HttpClientConfig<*>.() -> Unit = {},
): RawUploader = RawUploader(
    HttpClient { configure(configuration.site, configuration.apiKey, config) },
)

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.configure(
    site: Site,
    apiKey: String,
    config: HttpClientConfig<T>.() -> Unit,
) {
    apply(config)

    install(ContentNegotiation) {
        json(Json)
    }

    defaultRequest {
        host = site.logHost
        header("DD-API-KEY", apiKey)
        contentType(ContentType.Application.Json)
        this.url { protocol = URLProtocol.HTTPS }
    }
}
