package com.juul.datadog

public interface Initializer {

    /**
     * To be compliant with the GDPR regulation, the Datadog SDK requires the [trackingConsent]
     * value at initialization.
     *
     * [trackingConsent] is only used on Android and iOS. It is ignored on JavaScript.
     */
    public fun initialize(
        trackingConsent: TrackingConsent,
        onReady: (() -> Unit)? = null,
    )

    /**
     * Should only be called after Datadog is [initialized][initialize].
     *
     * No-ops on JavaScript.
     */
    public fun setTrackingConsent(trackingConsent: TrackingConsent)
}
