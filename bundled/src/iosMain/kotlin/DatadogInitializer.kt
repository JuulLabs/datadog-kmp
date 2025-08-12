package com.juul.datadog

import cocoapods.DatadogObjc.DDConfiguration
import cocoapods.DatadogObjc.DDDatadog
import cocoapods.DatadogObjc.DDLogs
import cocoapods.DatadogObjc.DDLogsConfiguration
import cocoapods.DatadogObjc.DDSite
import cocoapods.DatadogObjc.DDTrackingConsent

public actual class DatadogInitializer actual constructor(
    private val configuration: Configuration,
) : Initializer {

    actual override fun initialize(trackingConsent: TrackingConsent, onReady: (() -> Unit)?) {
        DDDatadog.initializeWithConfiguration(
            DDConfiguration(configuration),
            trackingConsent.toDatadogType(),
        )
        if (configuration.features.logs) DDLogs.enableWith(DDLogsConfiguration(null))
        onReady?.invoke()
    }

    actual override fun setTrackingConsent(trackingConsent: TrackingConsent) {
        DDDatadog.setTrackingConsentWithConsent(trackingConsent.toDatadogType())
    }
}

private fun DDConfiguration(
    configuration: Configuration,
): DDConfiguration = DDConfiguration(configuration.clientToken, configuration.environment).apply {
    setService(configuration.service)
    setSite(configuration.site.toDatadogType())
}

private fun Site.toDatadogType(): DDSite = when (this) {
    Site.US1 -> DDSite.us1()
    Site.US3 -> DDSite.us3()
    Site.US5 -> DDSite.us5()
    Site.AP1 -> DDSite.ap1()
    Site.EU -> DDSite.eu1()
    Site.US1_FED -> DDSite.us1_fed()
}

private fun TrackingConsent.toDatadogType(): DDTrackingConsent = when (this) {
    TrackingConsent.NotGranted -> DDTrackingConsent.notGranted()
    TrackingConsent.Pending -> DDTrackingConsent.pending()
    TrackingConsent.Granted -> DDTrackingConsent.granted()
}
