package com.juul.datadog.sample.android

import android.app.Application
import com.juul.datadog.TrackingConsent.Granted
import com.juul.datadog.sample.initializeDatadog

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDatadog(Granted)
    }
}
