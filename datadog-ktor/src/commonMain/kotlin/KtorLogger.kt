package com.juul.datadog.ktor

import com.juul.datadog.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class KtorLogger(
    private val scope: CoroutineScope,
    private val logSubmitter: LogSubmission,
    private val config: LogConfiguration,
    private val clock: Clock = Clock.System,
) : RestLogger {
    private val globalAttributes = MutableStateFlow(mutableMapOf<String, Any?>())
    private val keyedTags = MutableStateFlow(mutableMapOf<String, String>())
    private val unkeyedTags = MutableStateFlow(mutableListOf<String>())

    override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val tagString = keyedTags.value.entries
            .map { (key, value) ->
                "$key:$value"
            }.plus(unkeyedTags)
            .joinToString(separator = ",")

        val log = buildJsonObject {
            // Add global attributes
            globalAttributes.value.forEach { entry -> put(entry.key, wrapInJsonPrimitive(entry.value)) }
            // Add event-specific attributes
            attributes?.forEach { entry -> put(entry.key, wrapInJsonPrimitive(entry.value)) }
            // Add reserved attributes
            put("message", message)
            if (config.source != null) put("ddsource", config.source)
            if (tagString.isNotEmpty()) put("ddtags", tagString)
            if (config.host != null) put("hostname", config.host)
            if (config.service != null) put("service", config.service)
            put("status", level.name.lowercase())
            put("timestamp", clock.now().toEpochMilliseconds())
        }
        scope.launch { logSubmitter.submitLogs(listOf(log)) }
    }

    override fun debug(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        log(Logger.Level.Debug, message, attributes, throwable)
    }

    override fun info(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        log(Logger.Level.Info, message, attributes, throwable)
    }

    override fun warn(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        log(Logger.Level.Warn, message, attributes, throwable)
    }

    override fun error(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        log(Logger.Level.Error, message, attributes, throwable)
    }

    override fun assert(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        log(Logger.Level.Assert, message, attributes, throwable)
    }

    override fun verbose(message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        log(Logger.Level.Verbose, message, attributes, throwable)
    }

    override fun addTag(tag: String) {
        unkeyedTags.update { tags ->
            tags.add(tag)
            return@update tags
        }
    }

    override fun removeTag(tag: String) {
        unkeyedTags.update { tags ->
            tags.remove(tag)
            return@update tags
        }
    }

    override fun addTagWithKey(key: String, value: String) {
        keyedTags.update { tags ->
            tags[key] = value
            return@update tags
        }
    }

    override fun removeTagsWithKey(key: String) {
        keyedTags.update { tags ->
            tags.remove(key)
            return@update tags
        }
    }

    override fun addAttribute(key: String, value: String) {
        globalAttributes.update { attributes ->
            attributes[key] = value
            return@update attributes
        }
    }

    override fun removeAttribute(key: String) {
        globalAttributes.update { attributes ->
            attributes.remove(key)
            return@update attributes
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun wrapInJsonPrimitive(value: Any?): JsonPrimitive =
        when (value) {
            null -> JsonNull
            is Boolean -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is String -> JsonPrimitive(value)
            is UByte -> JsonPrimitive(value)
            is UShort -> JsonPrimitive(value)
            is UInt -> JsonPrimitive(value)
            is ULong -> JsonPrimitive(value)
            else -> JsonPrimitive(value.toString())
        }
}
