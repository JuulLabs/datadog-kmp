package com.juul.datadog.ktor

import com.juul.datadog.Site

public data class Configuration(
    public val rest: Rest,
    public val log: Log = Log(),
) {
    public data class Rest(
        public val apiKey: String,
        public val site: Site,
    )

    public data class Log(
        public val source: String? = null,
        public val host: String? = null,
        public val service: String? = null,
    )
}
