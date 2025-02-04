package com.juul.datadog

public sealed class Site(
    public val apiHost: String,
    public val logHost: String,
) {
    public data object US1 : Site(
        apiHost = "api.datadoghq.com",
        logHost = "http-intake.logs.datadoghq.com",
    )
    public data object US3 : Site(
        apiHost = "api.us3.datadoghq.com",
        logHost = "http-intake.logs.us3.datadoghq.com",
    )
    public data object US5 : Site(
        apiHost = "api.us5.datadoghq.com",
        logHost = "http-intake.logs.us5.datadoghq.com",
    )
    public data object EU : Site(
        apiHost = "api.datadoghq.eu",
        logHost = "http-intake.logs.eu.datadoghq.com",
    )
    public data object AP1 : Site(
        apiHost = "api.ap1.datadoghq.com",
        logHost = "http-intake.logs.ap1.datadoghq.com",
    )
    public data object US1_FED : Site(
        apiHost = "api.ddog-gov.com",
        logHost = "http-intake.logs.ddog-gov.com",
    )
}
