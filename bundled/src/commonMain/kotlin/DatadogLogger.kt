package com.juul.datadog

public expect interface PlatformLogger : Logger

public expect class DatadogLogger(
    name: String,
    level: Logger.Level? = null,
    configuration: LoggerConfiguration? = null,
) : PlatformLogger {

    override fun log(
        level: Logger.Level,
        message: String,
        attributes: Map<String, Any?>?,
        throwable: Throwable?,
    )

    override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?)
    override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?)
    override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?)
    override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?)
    override fun addAttribute(key: String, value: String)
    override fun removeAttribute(key: String)
}
