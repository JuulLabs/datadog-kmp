package com.juul.datadog.ktor

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.JsonObject

private const val LOGS_URI = "api/v2/logs"

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
