package com.juul.datadog.sample

import com.juul.datadog.DatadogInitializer
import com.juul.datadog.DatadogLogger
import com.juul.datadog.TrackingConsent

public fun initializeDatadog(trackingConsent: TrackingConsent) {
    DatadogInitializer(configuration)
        .initialize(trackingConsent) {
            logger = DatadogLogger("root")
        }
}
