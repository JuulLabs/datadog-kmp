package com.juul.datadog

import kotlinx.serialization.json.JsonObject

public interface RawSink {
    public fun add(log: JsonObject)
}
