package com.juul.datadog

import com.juul.datadog.LoggerConfiguration.Handler.Http

public actual data class LoggerConfiguration(
    public val handler: Handler = Http,
    public val context: Map<String, Any?> = emptyMap(),
) {

    public enum class Handler {

        /** Send logs to the console and Datadog (http) */
        Http,

        /** Send logs to the console only. */
        Console,

        /** Do not send logs at all. */
        Silent,
    }
}
