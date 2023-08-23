package com.juul.datadog

internal fun Map<String, Any?>.toJsObject(): dynamic {
    val obj = js("({})")
    for (entry in this) {
        val (key, value) = entry
        obj[key] = value
    }
    return obj
}
