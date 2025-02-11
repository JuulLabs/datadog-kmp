package com.juul.datadog.ktor

import com.github.michaelbull.result.onFailure
import com.juul.datadog.Logger
import com.juul.datadog.ktor.Configuration.Log
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal class KtorLogger(
    private val scope: CoroutineScope,
    private val logSubmitter: LogSubmission,
    private val config: Log,
    private val clock: Clock = Clock.System,
) : RestLogger {

    private inner class LoggerState(
        val globalAttributes: Map<String, Any?>,
        val keyedTags: Map<String, String>,
        val unkeyedTags: List<String>,
    ) {
        val tagString: String by lazy {
            keyedTags.entries
                .map { (key, value) ->
                    "$key:$value"
                }.plus(unkeyedTags)
                .joinToString(separator = ",")
        }

        val baseLogMap: PersistentMap<String, JsonElement> by lazy {
            persistentHashMapOf<String, JsonElement>().mutate { buffer ->
                globalAttributes.forEach { (key, value) -> buffer[key] = wrapInJsonPrimitive(value) }

                if (config.source != null) buffer["ddsource"] = JsonPrimitive(config.source)
                if (config.host != null) buffer["hostname"] = JsonPrimitive(config.host)
                if (config.service != null) buffer["service"] = JsonPrimitive(config.service)

                if (tagString.isNotEmpty()) buffer["ddtags"] = JsonPrimitive(tagString)
            }
        }
    }

    private val state = MutableStateFlow(
        LoggerState(emptyMap(), emptyMap(), emptyList()),
    )

    override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val log = JsonObject(
            state.value.baseLogMap.mutate { buffer ->
                // Add event-specific attributes
                attributes?.forEach { (key, value) -> buffer[key] = wrapInJsonPrimitive(value) }
                // Add reserved attributes
                buffer["message"] = JsonPrimitive(message)
                buffer["status"] = JsonPrimitive(level.name.lowercase())
                buffer["timestamp"] = JsonPrimitive(clock.now().toEpochMilliseconds())
            },
        )

        scope.launch {
            logSubmitter
                .submitLogs(listOf(log))
                .onFailure { println("Failure: ${it.message}") }
        }
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
        state.update {
            LoggerState(
                it.globalAttributes,
                it.keyedTags,
                it.unkeyedTags + tag,
            )
        }
    }

    override fun removeTag(tag: String) {
        state.update {
            LoggerState(
                it.globalAttributes,
                it.keyedTags,
                it.unkeyedTags - tag,
            )
        }
    }

    override fun addTagWithKey(key: String, value: String) {
        state.update {
            LoggerState(
                it.globalAttributes,
                it.keyedTags + (key to value),
                it.unkeyedTags,
            )
        }
    }

    override fun removeTagsWithKey(key: String) {
        state.update {
            LoggerState(
                it.globalAttributes,
                it.keyedTags - key,
                it.unkeyedTags,
            )
        }
    }

    override fun addAttribute(key: String, value: String) {
        state.update {
            LoggerState(
                it.globalAttributes + (key to value),
                it.keyedTags,
                it.unkeyedTags,
            )
        }
    }

    override fun removeAttribute(key: String) {
        state.update {
            LoggerState(
                it.globalAttributes - key,
                it.keyedTags,
                it.unkeyedTags,
            )
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
