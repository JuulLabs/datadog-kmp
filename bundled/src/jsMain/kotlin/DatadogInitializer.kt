package com.juul.datadog

import com.juul.datadog.TrackingConsent.Granted
import com.juul.datadog.external.datadogLogs

public actual class DatadogInitializer actual constructor(
    private val configuration: Configuration,
) : Initializer {

    public fun initialize(onReady: (() -> Unit)?) {
        initialize(Granted, onReady)
    }

    actual override fun initialize(trackingConsent: TrackingConsent, onReady: (() -> Unit)?) {
        if (onReady != null) {
            datadogLogs.onReady {
                init()
                onReady.invoke()
            }
        } else {
            init()
        }
    }

    private fun init() {
        datadogLogs.init(
            jso {
                clientToken = configuration.clientToken
                site = configuration.site.toDatadogType()
                env = configuration.environment
                configuration.version?.let { version = it }
                configuration.service?.let { service = it }
                forwardErrorsToLogs = configuration.forwardErrorsToLogs
                sessionSampleRate = configuration.sessionSampleRate
                configuration.beforeSend?.let { beforeSend = it }
            },
        )
    }

    actual override fun setTrackingConsent(trackingConsent: TrackingConsent) {
        // No-op
    }
}

private fun Site.toDatadogType(): String = when (this) {
    Site.US1 -> "datadoghq.com"
    Site.US3 -> "us3.datadoghq.com"
    Site.US5 -> "us5.datadoghq.com"
    Site.EU1 -> "datadoghq.eu"
    Site.US1_FED -> "ddog-gov.com"
}
