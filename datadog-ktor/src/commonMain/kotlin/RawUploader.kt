package com.juul.datadog.ktor

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.juul.datadog.Site
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
    uploaderConfig: Configuration.Rest,
    engine: HttpClientEngineFactory<T>,
    httpConfig: HttpClientConfig<T>.() -> Unit = {},
): RawUploader = RawUploader(
    HttpClient(engine) { configure(uploaderConfig.site, uploaderConfig.apiKey, httpConfig) },
)

public fun RawUploader(
    uploaderConfig: Configuration.Rest,
    httpConfig: HttpClientConfig<*>.() -> Unit = {},
): RawUploader = RawUploader(
    HttpClient { configure(uploaderConfig.site, uploaderConfig.apiKey, httpConfig) },
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
