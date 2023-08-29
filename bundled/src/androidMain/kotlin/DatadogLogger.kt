package com.juul.datadog

import android.util.Log
import com.datadog.android.log.Logger as DatadogLogger

private const val ALL = -1

public actual class DatadogLogger actual constructor(
    name: String,
    level: Logger.Level?,
    configuration: LoggerConfiguration?,
) : AndroidLogger {

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
}

private fun Logger.Level.toDatadogType(): Int = when (this) {
    Logger.Level.Verbose -> Log.VERBOSE
    Logger.Level.Debug -> Log.DEBUG
    Logger.Level.Info -> Log.INFO
    Logger.Level.Notice, Logger.Level.Warn -> Log.WARN
    Logger.Level.Error -> Log.ERROR
    Logger.Level.Assert -> Log.ASSERT
}
