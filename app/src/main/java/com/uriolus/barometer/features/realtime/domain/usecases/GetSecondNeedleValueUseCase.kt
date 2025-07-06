package com.uriolus.barometer.features.realtime.domain.usecases

import com.uriolus.barometer.features.realtime.domain.repository.SecondNeedleRepository
import kotlinx.coroutines.flow.Flow

class GetSecondNeedleValueUseCase(private val repository: SecondNeedleRepository) {
    fun exec(): Flow<Float?> = repository.getSecondNeedleValue()
}
