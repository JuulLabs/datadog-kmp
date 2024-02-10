package com.juul.datadog

public interface Logger {
    public fun log(level: Level, message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)

    public fun debug(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
    public fun info(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
    public fun warn(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
    public fun error(message: String, attributes: Map<String, Any?>? = null, throwable: Throwable? = null)
    public fun addAttribute(key: String, value: String)

    public fun removeAttribute(key: String)

    public enum class Level {
        Verbose, // Android only, maps to Debug on iOS & JS.
        Debug,
        Info,
        Notice, // iOS only, maps to Warn on Android & JS.
        Warn,
        Error,
        Assert, // iOS and Android only, maps to Error on JS. Referred to as Critical on iOS.
    }
}
