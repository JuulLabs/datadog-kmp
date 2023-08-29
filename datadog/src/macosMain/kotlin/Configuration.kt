package com.juul.datadog

/** Note: Native darwin (e.g. MacOS) is not supported, this class is provided for No-op purposes. */
public actual data class Configuration(
    public actual val clientToken: String,
    public actual val site: Site,
    public actual val environment: String,
    public actual val service: String? = null,
)
