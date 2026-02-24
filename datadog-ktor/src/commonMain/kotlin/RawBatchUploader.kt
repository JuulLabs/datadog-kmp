package com.juul.datadog.ktor

import com.github.michaelbull.result.onFailure
import com.juul.datadog.RawSource
import com.juul.datadog.TrackingConsent
import com.juul.datadog.TrackingConsent.Granted
import com.juul.datadog.TrackingConsent.NotGranted
import com.juul.datadog.TrackingConsent.Pending
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public class RawBatchUploader(
    private val source: RawSource,
    private val uploader: RawUploader,
    private val uploadInterval: Duration = 5.seconds,
    private val batchSize: Int = 100,
) {

    private val state = MutableStateFlow<TrackingConsent>(Pending)

    public fun setTrackingConsent(trackingConsent: TrackingConsent) {
        state.value = trackingConsent
    }

    public fun launchIn(scope: CoroutineScope, onFailure: (Throwable) -> Unit) {
        var job: Job? = null
        state
            .onEach {
                job?.cancelAndJoin()
                job = null
                when (it) {
                    Granted -> job = scope.launchUpload(onFailure)
                    NotGranted -> source.clear()
                    Pending -> Unit // No-op
                }
            }
            .launchIn(scope)
    }

    private fun CoroutineScope.launchUpload(onFailure: (Throwable) -> Unit): Job = launch {
        while (coroutineContext.isActive) {
            val batch = source.remove(batchSize)
            if (batch.isNotEmpty()) {
                uploader
                    .send(batch)
                    .onFailure(onFailure)
            }
            delay(uploadInterval)
        }
    }
}
