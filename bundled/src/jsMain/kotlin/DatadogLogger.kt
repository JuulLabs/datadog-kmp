package com.juul.datadog

import com.juul.datadog.external.JsLoggerConfiguration
import com.juul.datadog.external.datadogLogs

public actual class DatadogLogger actual constructor(
    name: String,
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
) : BrowserLogger {

    private val logger = datadogLogs.createLogger(name, conf(level, configuration))

    override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.debug(message, attributes?.toJsObject(), throwable)
    }

    override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.info(message, attributes?.toJsObject(), throwable)
    }

    override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.warn(message, attributes?.toJsObject(), throwable)
    }

    override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.error(message, attributes?.toJsObject(), throwable)
    }
}

private fun conf(
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
): JsLoggerConfiguration = jso {
    if (level != null) this@jso.level = level.toDatadogType()
    if (configuration != null) {
        handler = configuration.handler.toDatadogType()
        context = configuration.context.toJsObject()
    }
}

private fun Logger.Level.toDatadogType() = when (this) {
    Logger.Level.Verbose, Logger.Level.Debug -> "debug"
    Logger.Level.Info -> "info"
    Logger.Level.Notice, Logger.Level.Warn -> "warn"
    Logger.Level.Error, Logger.Level.Assert -> "error"
}

private fun LoggerConfiguration.Handler.toDatadogType(): String = when (this) {
    LoggerConfiguration.Handler.Http -> "http"
    LoggerConfiguration.Handler.Console -> "console"
    LoggerConfiguration.Handler.Silent -> "silent"
}
