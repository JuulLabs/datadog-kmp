package com.juul.datadog

public interface JvmLogger : Logger {
    public fun assert(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)

    @Deprecated(
        "Removed. Use `debug`.",
        replaceWith = ReplaceWith("debug(message, attributes, throwable)"),
    )
    public fun verbose(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
}
