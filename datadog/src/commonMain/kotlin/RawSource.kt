package com.juul.datadog

import kotlinx.serialization.json.JsonObject

public interface RawSource {

    /** Removes the specified [count] of logs from the [RawSource] and returns them. */
    public fun remove(count: Int): List<JsonObject>

    public fun clear()
}
