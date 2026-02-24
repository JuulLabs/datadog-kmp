package com.juul.datadog.ktor

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

private const val LOGS_URI = "api/v2/logs"

public fun <T : HttpClientEngineConfig> RawUploader(
    endpointConfig: Configuration.Endpoint,
    engine: HttpClientEngineFactory<T>,
    httpConfig: HttpClientConfig<T>.() -> Unit = {},
): RawUploader = RawUploader(
    HttpClient(engine) { configure(endpointConfig, httpConfig) },
)

public fun RawUploader(
    endpointConfig: Configuration.Endpoint,
    httpConfig: HttpClientConfig<*>.() -> Unit = {},
): RawUploader = RawUploader(
    HttpClient { configure(endpointConfig, httpConfig) },
)

public class RawUploader internal constructor(
    private val http: HttpClient,
) {

    public suspend fun send(
        logs: List<JsonObject>,
    ): Result<Unit, Throwable> = http
        .preparePost(LOGS_URI) {
            setBody(logs)
        }.executeForResult()
        .body<Unit>()
}

private suspend inline fun <reified T> Result<HttpResponse, Throwable>.body(): Result<T, Throwable> =
    map { it.body() }

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.configure(
    endpointConfig: Configuration.Endpoint,
    config: HttpClientConfig<T>.() -> Unit,
) {
    apply(config)

    install(ContentNegotiation) {
        json(Json)
    }

    defaultRequest {
        host = when (endpointConfig) {
            is Configuration.Endpoint.Rest -> endpointConfig.site.logHost
            is Configuration.Endpoint.BrowserIntake -> endpointConfig.site.browserIntakeHost
        }

        if (endpointConfig is Configuration.Endpoint.Rest) {
            header("DD-API-KEY", endpointConfig.apiKey)
        } else if (endpointConfig is Configuration.Endpoint.BrowserIntake) {
            this.url {
                this.parameters.append("dd-api-key", endpointConfig.clientToken)
            }
        }
        contentType(ContentType.Application.Json)
        this.url { protocol = URLProtocol.HTTPS }
    }
}
