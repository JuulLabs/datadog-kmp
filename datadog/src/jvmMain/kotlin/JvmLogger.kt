package com.juul.datadog

public interface JvmLogger : Logger {
    public fun verbose(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
}
