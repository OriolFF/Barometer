package com.uriolus.barometer.domain.usecases

import com.uriolus.barometer.domain.model.BarometerReading
import com.uriolus.barometer.domain.repository.BarometerRepository
import kotlinx.coroutines.flow.Flow

class SubscribeBarometerUseCase(private val repository: BarometerRepository) {
    operator fun invoke(): Flow<BarometerReading> {
        return repository.getBarometerReadings()
    }
}
