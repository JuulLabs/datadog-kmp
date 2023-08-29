package com.juul.datadog

public enum class TrackingConsent {

    /**
     * The permission to persist and dispatch data to the Datadog Endpoints was granted. Any
     * previously stored pending data will be marked as ready for sent.
     */
    Granted,

    /**
     * Any previously stored pending data will be deleted and any Log, Rum, Trace event will be
     * dropped from now on without persisting it in any way.
     */
    NotGranted,

    /**
     * Any Log, Rum, Trace event will be persisted in a special location and will be pending there
     * until we will receive one of the [Granted] or [NotGranted] flags. Based on the value of the
     * consent flag we will decide what to do with the pending stored data.
     */
    Pending,
}
