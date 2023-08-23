package com.juul.datadog

/**
 * - Android [Advanced Logging](https://docs.datadoghq.com/logs/log_collection/android/?tab=kotlin#advanced-logging)
 * - iOS [Advanced Logging](https://docs.datadoghq.com/logs/log_collection/ios/?tab=cocoapods#advanced-logging)
 */
public actual data class LoggerConfiguration(

    /**
     * Sets the value for the service
     * [standard attribute](https://docs.datadoghq.com/logs/log_configuration/attributes_naming_convention/)
     * attached to all logs sent to Datadog.
     */
    val serviceName: String?,

    /**
     * Add `network.client.*` attributes to all logs. The data logged by default is: `reachability`
     * (yes, no, maybe), `available_interfaces` (wifi, cellular, and more), `sim_carrier.name`
     * (for example: AT&T - US), `sim_carrier.technology` (3G, LTE, and more) and
     * `sim_carrier.iso_country` (for example: US).
     */
    val networkInfoEnabled: Boolean = false,

    val bundleWithRumEnabled: Boolean = true,
    val bundleWithTraceEnabled: Boolean = true,
    val remoteSampleRate: Float,

    /** Whether to log to logcat on Android or console on iOS. */
    public val logToConsole: Boolean = false,
)
