package com.juul.datadog

public expect class Configuration {
    public val clientToken: String
    public val site: Site
    public val environment: String
    public val service: String?
}
