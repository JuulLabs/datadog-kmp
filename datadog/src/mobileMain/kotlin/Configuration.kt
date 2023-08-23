package com.juul.datadog

import com.juul.datadog.Configuration.UploadFrequency.Average

private val allFeatures = Configuration.Features(
    logs = true,
)

public actual data class Configuration(
    public actual val clientToken: String,
    public actual val site: Site,
    public actual val environment: String,
    public actual val service: String? = null,
    public val features: Features = allFeatures,
    public val uploadFrequency: UploadFrequency? = Average,
) {

    public data class Features(
        val logs: Boolean = true,
    )

    public enum class UploadFrequency { Frequent, Average, Rare }
}
