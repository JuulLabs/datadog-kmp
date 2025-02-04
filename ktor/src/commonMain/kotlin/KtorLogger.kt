package com.juul.datadog.ktor

import com.juul.datadog.Logger
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.CoroutineScope
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

    private val guard = ReentrantLock()

    private val globalAttributeMap = mutableMapOf<String, Any?>()
    private val keyedTagMap = mutableMapOf<String, String>()
    private val unkeyedTags = mutableListOf<String>()

    override fun log(level: Logger.Level, message: String, attributes: Map<String, Any?>?, throwable: Throwable?) {
        val log = guard.withLock {
            val tagString = keyedTagMap.entries
                .map { (key, value) ->
                    "$key:$value"
                }.plus(unkeyedTags)
                .joinToString(separator = ",")

            buildJsonObject {
                // Add global attributes
                globalAttributeMap.forEach { entry -> put(entry.key, wrapInJsonPrimitive(entry.value)) }
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
        guard.withLock {
            unkeyedTags.add(tag)
        }
    }

    override fun removeTag(tag: String) {
        guard.withLock {
            unkeyedTags.remove(tag)
        }
    }

    override fun addTagWithKey(key: String, value: String) {
        guard.withLock {
            keyedTagMap[key] = value
        }
    }

    override fun removeTagsWithKey(key: String) {
        guard.withLock {
            keyedTagMap.remove(key)
        }
    }

    override fun addAttribute(key: String, value: String) {
        guard.withLock {
            globalAttributeMap.put(key, value)
        }
    }

    override fun removeAttribute(key: String) {
        guard.withLock {
            globalAttributeMap.remove(key)
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
