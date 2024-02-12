package com.juul.datadog

import com.juul.datadog.Logger.Level.Assert
import com.juul.datadog.Logger.Level.Debug
import com.juul.datadog.Logger.Level.Error
import com.juul.datadog.Logger.Level.Info
import com.juul.datadog.Logger.Level.Notice
import com.juul.datadog.Logger.Level.Verbose
import com.juul.datadog.Logger.Level.Warn
import com.juul.datadog.external.JsLoggerConfiguration
import com.juul.datadog.external.datadogLogs

public actual class DatadogLogger actual constructor(
    name: String,
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
) : BrowserLogger {

    private val logger = datadogLogs.createLogger(name, conf(level, configuration))
    override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        when (level) {
            Verbose, Debug -> debug(message, attributes, throwable)
            Info -> info(message, attributes, throwable)
            Notice, Warn -> warn(message, attributes, throwable)
            Error, Assert -> error(message, attributes, throwable)
        }
    }

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

    override fun addAttribute(key: String, value: String) {
        datadogLogs.setGlobalContextProperty(key, value)
    }

    override fun removeAttribute(key: String) {
        datadogLogs.removeGlobalContextProperty(key)
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
    Verbose, Debug -> "debug"
    Info -> "info"
    Notice, Warn -> "warn"
    Error, Assert -> "error"
}

private fun LoggerConfiguration.Handler.toDatadogType(): String = when (this) {
    LoggerConfiguration.Handler.Http -> "http"
    LoggerConfiguration.Handler.Console -> "console"
    LoggerConfiguration.Handler.Silent -> "silent"
}
