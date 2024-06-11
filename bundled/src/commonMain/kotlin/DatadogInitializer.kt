package com.juul.datadog

public expect class DatadogInitializer(
    configuration: Configuration,
) : Initializer {

    override fun initialize(trackingConsent: TrackingConsent, onReady: (() -> Unit)?)
    override fun setTrackingConsent(trackingConsent: TrackingConsent)
}
