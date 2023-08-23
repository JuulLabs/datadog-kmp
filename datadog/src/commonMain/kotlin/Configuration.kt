package com.juul.datadog

public enum class Site { US1, US3, US5, EU1, US1_FED }

public expect class Configuration {
    public val clientToken: String
    public val site: Site
    public val environment: String
    public val service: String?
}
