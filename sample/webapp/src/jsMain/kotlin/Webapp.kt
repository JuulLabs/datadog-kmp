package com.juul.datadog.sample

import com.juul.datadog.TrackingConsent.Granted

@JsExport
public class Webapp {
    public fun init() {
        initializeDatadog(Granted)
    }

    public fun i(message: String) {
        info(message)
    }

    public fun e(
        message: String,
        exceptionMessage: String,
    ) {
        testException(message, exceptionMessage)
    }
}
