package com.juul.datadog

import android.util.Log
import com.juul.datadog.Logger.Level.Assert
import com.juul.datadog.Logger.Level.Debug
import com.juul.datadog.Logger.Level.Error
import com.juul.datadog.Logger.Level.Info
import com.juul.datadog.Logger.Level.Notice
import com.juul.datadog.Logger.Level.Verbose
import com.juul.datadog.Logger.Level.Warn
import com.datadog.android.log.Logger as DatadogLogger

private const val ALL = -1

public actual class DatadogLogger actual constructor(
    name: String,
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
) : JvmLogger, TagHandler {

    private val logger: DatadogLogger = DatadogLogger.Builder()
        .setName(name)
        .setRemoteLogThreshold(level?.toDatadogType() ?: ALL)
        .apply {
            if (configuration != null) {
                configuration.serviceName?.let(::setService)
                setNetworkInfoEnabled(configuration.networkInfoEnabled)
                setBundleWithRumEnabled(configuration.bundleWithRumEnabled)
                setBundleWithTraceEnabled(configuration.bundleWithTraceEnabled)
                setRemoteSampleRate(configuration.remoteSampleRate)
                setLogcatLogsEnabled(configuration.logToConsole)
            }
        }
        .build()

    override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
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
        logger.wtf(message, throwable, attributes.orEmpty())
    }

    override fun verbose(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.v(message, throwable, attributes.orEmpty())
    }

    override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.d(message, throwable, attributes.orEmpty())
    }

    override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.i(message, throwable, attributes.orEmpty())
    }

    override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.w(message, throwable, attributes.orEmpty())
    }

    override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        logger.e(message, throwable, attributes.orEmpty())
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

    override fun removeTagWithKey(key: String) {
        logger.removeTagsWithKey(key)
    }

    override fun addAttribute(key: String, value: String) {
        logger.addAttribute(key, value)
    }

    override fun removeAttribute(key: String) {
        logger.removeAttribute(key)
    }

}

private fun Logger.Level.toDatadogType(): Int = when (this) {
    Verbose -> Log.VERBOSE
    Debug -> Log.DEBUG
    Info -> Log.INFO
    Notice, Warn -> Log.WARN
    Error -> Log.ERROR
    Assert -> Log.ASSERT
}
