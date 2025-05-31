package com.juul.datadog.ktor

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.http.isSuccess

internal class HttpException(message: String? = null) : Exception(message)

internal suspend fun HttpStatement.executeForResult(): Result<HttpResponse, Throwable> =
    try {
        execute().run {
            if (status.isSuccess()) {
                Ok(this)
            } else {
                Err(HttpException("Failed to submit Datadog logs, status: $status"))
            }
        }
    } catch (e: Throwable) {
        Err(e)
    }
