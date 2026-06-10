package com.juul.datadog.sample

import com.juul.datadog.Logger

internal lateinit var logger: Logger

public fun info(message: String) {
    logger.info(message, mapOf("from" to "lib"))
}

public fun testException(
    message: String,
    exceptionMessage: String,
) {
    try {
        error(exceptionMessage)
    } catch (e: Exception) {
        logger.error(message, mapOf("from" to "lib"), throwable = e)
    }
}
