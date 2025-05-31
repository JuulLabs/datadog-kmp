package com.juul.datadog

import com.datadog.kmp.log.LogLevel
import com.juul.datadog.Logger.Level.Assert
import com.juul.datadog.Logger.Level.Debug
import com.juul.datadog.Logger.Level.Error
import com.juul.datadog.Logger.Level.Info
import com.juul.datadog.Logger.Level.Notice
import com.juul.datadog.Logger.Level.Verbose
import com.juul.datadog.Logger.Level.Warn
import com.datadog.kmp.log.Logger as DatadogMultiplatformLogger

public actual class DatadogLogger actual constructor(
    name: String,
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
) : JvmLogger, TagHandler {

    private val logger: DatadogMultiplatformLogger = DatadogMultiplatformLogger
        .Builder()
        .setName(name)
        .setRemoteLogThreshold(level?.toDatadogType() ?: LogLevel.CRITICAL)
        .apply {
            if (configuration != null) {
                configuration.serviceName?.let(::setService)
                setNetworkInfoEnabled(configuration.networkInfoEnabled)
                setBundleWithRumEnabled(configuration.bundleWithRumEnabled)
                setBundleWithTraceEnabled(configuration.bundleWithTraceEnabled)
                setRemoteSampleRate(configuration.remoteSampleRate)
                setPrintLogsToConsole(configuration.logToConsole)
            }
        }.build()

    actual override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        when (level) {
            Verbose -> verbose(message, attributes, throwable)
            Debug -> debug(message, attributes, throwable)
            Info -> info(message, attributes, throwable)
            Notice, Warn -> warn(message, attributes, throwable)
            Error -> error(message, attributes, throwable)
            Assert -> assert(message, attributes, throwable)
        }
    }

    override fun assert(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.critical(message, throwable, attributes.orEmpty())
    }

    @Deprecated(
        "Removed. Use `debug`.",
        replaceWith = ReplaceWith("debug(message, attributes, throwable)"),
    )
    override fun verbose(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        debug(message, attributes.orEmpty(), throwable)
    }

    actual override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.debug(message, throwable, attributes.orEmpty())
    }

    actual override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.info(message, throwable, attributes.orEmpty())
    }

    actual override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.warn(message, throwable, attributes.orEmpty())
    }

    actual override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.error(message, throwable, attributes.orEmpty())
    }

    override fun addTag(tag: String) {
        logger.addTag(tag)
    }

    override fun removeTag(tag: String) {
        logger.removeTag(tag)
    }

    override fun addTagWithKey(key: String, value: String) {
        logger.addTag(key, value)
    }

    override fun removeTagsWithKey(key: String) {
        logger.removeTagsWithKey(key)
    }

    actual override fun addAttribute(key: String, value: String) {
        logger.addAttribute(key, value)
    }

    actual override fun removeAttribute(key: String) {
        logger.removeAttribute(key)
    }
}

private fun Logger.Level.toDatadogType(): LogLevel = when (this) {
    Verbose -> LogLevel.DEBUG
    Debug -> LogLevel.DEBUG
    Info -> LogLevel.INFO
    Notice, Warn -> LogLevel.WARN
    Error -> LogLevel.ERROR
    Assert -> LogLevel.CRITICAL
}
