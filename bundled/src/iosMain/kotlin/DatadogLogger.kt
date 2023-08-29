package com.juul.datadog

import cocoapods.DatadogObjc.DDLogLevel
import cocoapods.DatadogObjc.DDLogLevelCritical
import cocoapods.DatadogObjc.DDLogLevelDebug
import cocoapods.DatadogObjc.DDLogLevelError
import cocoapods.DatadogObjc.DDLogLevelInfo
import cocoapods.DatadogObjc.DDLogLevelNotice
import cocoapods.DatadogObjc.DDLogLevelWarn
import cocoapods.DatadogObjc.DDLogger
import cocoapods.DatadogObjc.DDLoggerConfiguration
import com.rickclephas.kmp.nserrorkt.asNSError

/**
 * Use `level` constructor parameter to set the minimum log level reported to Datadog servers. Any
 * log with a level equal or above the threshold will be sent. Defaults to [Logger.Level.Debug] if
 * `null`. Note: This setting doesn't impact logs printed to the console if
 * [LoggerConfiguration.logToConsole] is `true` — all logs will be printed, no matter of their level.
 */
public actual class DatadogLogger actual constructor(
    name: String,
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
) : IosLogger {

    private val logger = DDLogger.createWith(
        DDLoggerConfiguration(name, level ?: Logger.Level.Debug, configuration),
    )

    override fun notice(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val attrs = attributes.orEmpty() as Map<Any?, *>
        if (throwable == null) {
            logger.notice(message, attrs)
        } else {
            logger.notice(message, throwable.asNSError(), attrs)
        }
    }

    override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val attrs = attributes.orEmpty() as Map<Any?, *>
        if (throwable == null) {
            logger.debug(message, attrs)
        } else {
            logger.debug(message, throwable.asNSError(), attrs)
        }
    }

    override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val attrs = attributes.orEmpty() as Map<Any?, *>
        if (throwable == null) {
            logger.info(message, attrs)
        } else {
            logger.info(message, throwable.asNSError(), attrs)
        }
    }

    override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val attrs = attributes.orEmpty() as Map<Any?, *>
        if (throwable == null) {
            logger.warn(message, attrs)
        } else {
            logger.warn(message, throwable.asNSError(), attrs)
        }
    }

    override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val attrs = attributes.orEmpty() as Map<Any?, *>
        if (throwable == null) {
            logger.error(message, attrs)
        } else {
            logger.error(message, throwable.asNSError(), attrs)
        }
    }
}

private fun DDLoggerConfiguration(
    name: String,
    level: Logger.Level,
    configuration: LoggerConfiguration?,
) = if (configuration == null) {
    DDLoggerConfiguration().apply {
        setName(name)
        setRemoteLogThreshold(level.toDatadogType())
    }
} else {
    DDLoggerConfiguration(
        configuration.serviceName,
        name,
        configuration.networkInfoEnabled,
        configuration.bundleWithRumEnabled,
        configuration.bundleWithTraceEnabled,
        configuration.remoteSampleRate,
        level.toDatadogType(),
        configuration.logToConsole,
    )
}

private fun Logger.Level.toDatadogType(): DDLogLevel = when (this) {
    Logger.Level.Verbose, Logger.Level.Debug -> DDLogLevelDebug
    Logger.Level.Info -> DDLogLevelInfo
    Logger.Level.Notice -> DDLogLevelNotice
    Logger.Level.Warn -> DDLogLevelWarn
    Logger.Level.Error -> DDLogLevelError
    Logger.Level.Assert -> DDLogLevelCritical
}
