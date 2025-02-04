package com.juul.datadog.ktor

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrThrow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject

internal class HttpError(message: String? = null) : Throwable(message)

internal interface LogSubmission {
    suspend fun submitLogs(logs: List<JsonObject>): Result<Unit, Throwable>
}

internal class DatadogLogSubmission(
    private val http: HttpClient,
    private val restConfiguration: RestConfiguration,
) : LogSubmission {
    override suspend fun submitLogs(logs: List<JsonObject>): Result<Unit, Throwable> =
        http
            .preparePost("api/v2/logs") {
                host = restConfiguration.site.logHost
                header("DD-API-KEY", restConfiguration.apiKey)
                contentType(ContentType.Application.Json)
                url { this.protocol = URLProtocol.HTTPS }

                setBody(logs)
            }.executeForResult()
            .body<Unit>()
}

internal suspend fun HttpStatement.executeForResult(): Result<HttpResponse, Throwable> =
    try {
        execute().run {
            if (status.isSuccess()) {
                Ok(this)
            } else {
                Err(HttpError("Failed to submit Datadog logs, status: $status"))
            }
        }
    } catch (e: Throwable) {
        Err(e)
    }

internal suspend inline fun <reified T> Result<HttpResponse, Throwable>.body(): Result<T, Throwable> =
    try {
        Ok(this.getOrThrow().body())
    } catch (e: Exception) {
        Err(e)
    }
