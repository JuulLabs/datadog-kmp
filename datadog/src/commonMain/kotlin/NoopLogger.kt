package com.juul.datadog

public class NoopLogger : Logger {
    override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        // No-op
    }

    override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        // No-op
    }

    override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        // No-op
    }

    override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        // No-op
    }

    override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        // No-op
    }

    override fun addAttribute(key: String, value: String) {
        // No-op
    }

    override fun removeAttribute(key: String) {
        // No-op
    }
}
