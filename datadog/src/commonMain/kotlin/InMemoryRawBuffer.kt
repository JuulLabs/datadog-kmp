package com.juul.datadog

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.serialization.json.JsonObject

public class InMemoryRawBuffer : RawBuffer {

    private val lock = SynchronizedObject()
    private val buffer = mutableListOf<JsonObject>()

    override fun add(log: JsonObject) {
        synchronized(lock) {
            buffer.add(log)
        }
    }

    override fun remove(count: Int): List<JsonObject> {
        val logs = mutableListOf<JsonObject>()
        return synchronized(lock) {
            repeat(count) {
                logs += buffer.removeFirstOrNull() ?: return@repeat
            }
            logs.toList()
        }
    }
}
