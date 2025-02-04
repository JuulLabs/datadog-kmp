package com.juul.datadog.ktor

import com.juul.datadog.Logger
import com.juul.datadog.TagHandler

public interface RestLogger : Logger, TagHandler {
    public fun assert(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
    public fun verbose(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
}
