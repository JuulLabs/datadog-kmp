package com.juul.datadog

public interface IosLogger : Logger {
    public fun notice(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
}
