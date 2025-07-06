package com.uriolus.barometer.features.secondneedle.domain.usecases

import com.uriolus.barometer.features.secondneedle.domain.repository.SecondNeedleRepository
import kotlinx.coroutines.flow.Flow

class GetSecondNeedleValueUseCase(private val repository: SecondNeedleRepository) {
    fun exec(): Flow<Float?> = repository.getSecondNeedleValue()
}
