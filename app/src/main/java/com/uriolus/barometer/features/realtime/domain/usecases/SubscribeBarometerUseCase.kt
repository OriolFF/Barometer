package com.uriolus.barometer.features.realtime.domain.usecases

import com.uriolus.barometer.features.realtime.domain.model.BarometerReading
import com.uriolus.barometer.features.realtime.domain.repository.BarometerRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.transform
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SubscribeBarometerUseCase(private val repository: BarometerRepository) {
    /**
     * Subscribe to barometer readings with threshold filters
     * @param pressureThreshold The minimum pressure difference in hPa to consider a change significant (default: 0.01)
     * @param timeThreshold The minimum time between emissions in milliseconds (default: 500ms)
     * @return Flow of barometer readings with filtered insignificant variations
     */
    @OptIn(FlowPreview::class)
    fun exec(
        pressureThreshold: Float = 0.01f,
        timeThreshold: Duration = 500.milliseconds
    ): Flow<BarometerReading> {
        var lastEmittedReading: BarometerReading? = null
        return repository.getBarometerReadings()
            // Apply a stateful filter to emit only significant pressure changes
            .transform { newReading ->
                val lastReading = lastEmittedReading
                val isSignificantChange = lastReading == null ||
                    abs(newReading.pressure - lastReading.pressure) >= pressureThreshold

                if (isSignificantChange) {
                    lastEmittedReading = newReading
                    emit(newReading)
                }
            }
            // Then apply time-based debouncing
            .debounce(timeThreshold)
    }
}
