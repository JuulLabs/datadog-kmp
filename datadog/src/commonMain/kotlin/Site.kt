package com.juul.datadog

public enum class Site(
    public val apiHost: String,
    public val logHost: String,
) {
    US1(
        apiHost = "api.datadoghq.com",
        logHost = "http-intake.logs.datadoghq.com",
    ),
    US3(
        apiHost = "api.us3.datadoghq.com",
        logHost = "http-intake.logs.us3.datadoghq.com",
    ),
    US5(
        apiHost = "api.us5.datadoghq.com",
        logHost = "http-intake.logs.us5.datadoghq.com",
    ),
    EU(
        apiHost = "api.datadoghq.eu",
        logHost = "http-intake.logs.eu.datadoghq.com",
    ),
    AP1(
        apiHost = "api.ap1.datadoghq.com",
        logHost = "http-intake.logs.ap1.datadoghq.com",
    ),
    US1_FED(
        apiHost = "api.ddog-gov.com",
        logHost = "http-intake.logs.ddog-gov.com",
    ),
}
