package com.juul.datadog.ktor

import com.juul.datadog.Site

public data class Configuration(
    public val endpoint: Endpoint,
    public val log: Log = Log(),
) {

    /**
     * Configures the URL and authentication scheme to use. Generally, [Rest] should by used by backends
     * (as it uses a fully-fledged API token, which is not considered safe to expose to clients). Use
     * [BrowserIntake] for web clients, which uses a "client token" much like the bundled libraries.
     */
    public sealed interface Endpoint {
        public val site: Site

        public data class Rest(
            public override val site: Site,
            public val apiKey: String,
        ) : Endpoint

        public data class BrowserIntake(
            public override val site: Site,
            public val clientToken: String,
        ) : Endpoint
    }

    public data class Log(
        public val source: String? = null,
        public val host: String? = null,
        public val service: String? = null,
    )
}
