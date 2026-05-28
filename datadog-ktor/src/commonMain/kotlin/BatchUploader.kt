package com.juul.datadog.ktor

import com.juul.datadog.TrackingConsent
import kotlinx.coroutines.CoroutineScope

public interface BatchUploader {
    public fun setTrackingConsent(trackingConsent: TrackingConsent)
    public fun launchIn(scope: CoroutineScope, onFailure: (Throwable) -> Unit)
}
