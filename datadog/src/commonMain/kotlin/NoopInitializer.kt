package com.juul.datadog

public class NoopInitializer : Initializer {

    override fun initialize(trackingConsent: TrackingConsent, onReady: (() -> Unit)?) {
        // No-op
    }
}
