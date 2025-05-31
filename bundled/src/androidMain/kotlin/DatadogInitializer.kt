package com.juul.datadog

import com.datadog.kmp.Datadog
import com.datadog.kmp.DatadogSite
import com.datadog.kmp.log.Logs
import com.datadog.kmp.log.configuration.LogsConfiguration
import com.datadog.kmp.core.configuration.Configuration as DatadogConfiguration
import com.datadog.kmp.privacy.TrackingConsent as DatadogTrackingConsent

private const val NO_VARIANT = ""

/**
 * @param variant the variant of your application, which should be the value from your `BuildConfig.FLAVOR` constant if you have different flavors, [NO_VARIANT] otherwise.
 */
public actual class DatadogInitializer(
    private val configuration: Configuration,
    private val variant: String = NO_VARIANT,
) : Initializer {

    public actual constructor(configuration: Configuration) : this(configuration, NO_VARIANT)

    actual override fun initialize(trackingConsent: TrackingConsent, onReady: (() -> Unit)?) {
        Datadog.initialize(
            context = applicationContext,
            configuration = configuration.toDatadogType(variant),
            trackingConsent = trackingConsent.toDatadogType(),
        )
        if (configuration.features.logs) Logs.enable(LogsConfiguration.Builder().build())
        onReady?.invoke()
    }

    actual override fun setTrackingConsent(trackingConsent: TrackingConsent) {
        Datadog.setTrackingConsent(trackingConsent.toDatadogType())
    }
}

private fun Configuration.toDatadogType(variant: String): DatadogConfiguration =
    DatadogConfiguration
        .Builder(clientToken, environment, variant, service)
        .useSite(site.toDatadogType())
        .build()

private fun TrackingConsent.toDatadogType(): DatadogTrackingConsent = when (this) {
    TrackingConsent.Granted -> DatadogTrackingConsent.GRANTED
    TrackingConsent.NotGranted -> DatadogTrackingConsent.NOT_GRANTED
    TrackingConsent.Pending -> DatadogTrackingConsent.PENDING
}

private fun Site.toDatadogType(): DatadogSite = when (this) {
    Site.US1 -> DatadogSite.US1
    Site.US3 -> DatadogSite.US3
    Site.US5 -> DatadogSite.US5
    Site.AP1 -> DatadogSite.AP1
    Site.EU -> DatadogSite.EU1
    Site.US1_FED -> DatadogSite.US1_FED
}
