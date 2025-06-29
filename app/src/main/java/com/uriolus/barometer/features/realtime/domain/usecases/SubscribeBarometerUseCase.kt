package com.uriolus.barometer.features.realtime.domain.usecases

import com.uriolus.barometer.features.realtime.domain.model.BarometerReading
import com.uriolus.barometer.features.realtime.domain.repository.BarometerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlin.math.roundToInt

class SubscribeBarometerUseCase(private val repository: BarometerRepository) {
    operator fun invoke(): Flow<BarometerReading> =
        repository.getBarometerReadings()
            .distinctUntilChangedBy {
                // Compare rounded values to avoid reacting to insignificant fluctuations
                (it.pressure * 100).roundToInt()
            }
}
