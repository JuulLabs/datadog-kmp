@file:JsModule("@datadog/browser-logs")
@file:JsNonModule

package com.juul.datadog.external

public abstract external class JsConfiguration {
    public var clientToken: String
    public var site: String
    public var service: String?
    public var env: String?
    public var version: String?
    public var forwardErrorsToLogs: Boolean
    public var sessionSampleRate: Int
    public var beforeSend: (log: dynamic) -> dynamic
}

public abstract external class JsLoggerConfiguration {
    public var level: String?
    public var handler: String?
    public var context: dynamic
}

public external interface JsLogger {

    /**
     * [handler] can be one of:
     *
     * | Value   | Description                                     |
     * |---------|-------------------------------------------------|
     * | http    | Send logs to the `console` and Datadog (`http`) |
     * | console | Send logs to the console only                   |
     * | silent  | Not send logs at all (silent)                   |
     *
     * https://docs.datadoghq.com/logs/log_collection/javascript/#change-the-destination
     */
    public fun setHandler(handler: String)

    /**
     * [level] can be one of:
     *
     * - debug
     * - info
     * - warn
     * - error
     *
     * > Only logs with a status equal to or higher than the specified level are sent.
     *
     * https://docs.datadoghq.com/logs/log_collection/javascript/#filter-by-status
     */
    public fun setLevel(level: String)

    public fun debug(message: String, messageContext: dynamic, error: Throwable?)
    public fun info(message: String, messageContext: dynamic, error: Throwable?)
    public fun warn(message: String, messageContext: dynamic, error: Throwable?)
    public fun error(message: String, messageContext: dynamic, error: Throwable?)
}

public external interface DatadogLogs {

    public fun onReady(action: () -> Unit)

    /** Example [usage when using NPM](https://docs.datadoghq.com/logs/log_collection/javascript/#npm):
     *
     * ```
     * datadogLogs.init({
     *   clientToken: '<DATADOG_CLIENT_TOKEN>',
     *   site: '<DATADOG_SITE>',
     *   forwardErrorsToLogs: true,
     *   sessionSampleRate: 100,
     * })
     * ```
     *
     * The available initialization parameters can be found at:
     * https://docs.datadoghq.com/logs/log_collection/javascript/#initialization-parameters
     */
    public fun init(configuration: JsConfiguration)

    /**
     * [name] is the name of the logger and can be retrieved via [getLogger].
     *
     * [conf] should be of the form:
     * ```
     * {
     *   level?: 'debug' | 'info' | 'warn' | 'error',
     *   handler?: 'http' | 'console' | 'silent',
     *   context?: Context
     * }
     * ```
     *
     * See [JsLogger.setLevel] for possible values of `level`. See [JsLogger.setHandler] for
     * possible values of `handler`. `context` is an optional key-value pair, for example:
     * `{ env: 'staging' }`.
     *
     * https://docs.datadoghq.com/logs/log_collection/javascript/#npm-6
     */
    public fun createLogger(name: String, conf: JsLoggerConfiguration): JsLogger

    /** Retrieve logger by [name] that was previously created with [createLogger]. */
    public fun getLogger(name: String): JsLogger

    public val logger: JsLogger
}

public external val datadogLogs: DatadogLogs
