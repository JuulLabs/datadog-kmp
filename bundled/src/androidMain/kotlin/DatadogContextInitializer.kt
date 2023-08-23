package com.juul.datadog

import android.content.Context
import androidx.startup.Initializer

internal lateinit var applicationContext: Context
    private set

public object Datadog

public class DatadogContextInitializer : Initializer<Datadog> {

    override fun create(context: Context): Datadog {
        applicationContext = context.applicationContext
        return Datadog
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
