package com.juul.datadog

// Copied from:
// https://github.com/JetBrains/kotlin-wrappers/blob/91b2c1568ec6f779af5ec10d89b5e2cbdfe785ff/kotlin-extensions/src/main/kotlin/kotlinx/js/jso.kt

internal inline fun <T : Any> jso(): T = js("({})")
internal inline fun <T : Any> jso(block: T.() -> Unit): T = jso<T>().apply(block)
