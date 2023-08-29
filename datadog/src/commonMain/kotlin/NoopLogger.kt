package com.juul.datadog

public class NoopLogger : Logger {

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
}
