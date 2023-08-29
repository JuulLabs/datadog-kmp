package com.juul.datadog

public expect interface PlatformLogger : Logger

public expect class DatadogLogger(
    name: String,
    level: Logger.Level? = null,
    configuration: LoggerConfiguration? = null,
) : PlatformLogger
