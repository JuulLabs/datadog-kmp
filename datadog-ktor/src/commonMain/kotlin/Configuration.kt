package com.juul.datadog.ktor

import com.juul.datadog.Site

public data class DatadogKtorConfiguration(
    public val rest: RestConfiguration,
    public val log: LogConfiguration = LogConfiguration(),
)

public data class RestConfiguration(
    public val apiKey: String,
    public val site: Site,
)

public data class LogConfiguration(
    public val source: String? = null,
    public val host: String? = null,
    public val service: String? = null,
)
