package com.juul.datadog.sample

import com.juul.datadog.Configuration
import com.juul.datadog.Site.US1

public actual val configuration: Configuration =
    Configuration(
        clientToken = DATADOG_CLIENT_TOKEN,
        site = US1,
        environment = "staging",
        service = "com.juul.datadog.sample.webapp",
        version = "0.0.1",
    )
