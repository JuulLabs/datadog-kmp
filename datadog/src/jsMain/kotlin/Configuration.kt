package com.juul.datadog

public actual data class Configuration(
    public actual val clientToken: String,
    public actual val site: Site,
    public actual val environment: String,
    public actual val service: String? = null,
    public val version: String? = null,

    /**
     * Set to `false` to stop forwarding `console.error` logs, uncaught exceptions and network
     * errors to Datadog.
     */
    public val forwardErrorsToLogs: Boolean = true,

    /**
     * The percentage of sessions to track: `100` for all, `0` for none. Only tracked sessions send
     * logs.
     */
    public val sessionSampleRate: Int = 100,

    /**
     * The [beforeSend] callback function gives you access to each log collected by the Browser SDK
     * before it is sent to Datadog, and lets you update any property. Have [beforeSend] return
     * `false` to drop log.
     *
     * https://docs.datadoghq.com/logs/log_collection/javascript/#scrub-sensitive-data-from-your-browser-logs
     */
    public val beforeSend: ((log: dynamic) -> dynamic)? = null,
)
