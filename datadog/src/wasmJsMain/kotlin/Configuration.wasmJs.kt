package com.juul.datadog

public actual data class Configuration(
    actual val clientToken: String,
    actual val site: Site,
    actual val environment: String,
    actual val service: String?
)
