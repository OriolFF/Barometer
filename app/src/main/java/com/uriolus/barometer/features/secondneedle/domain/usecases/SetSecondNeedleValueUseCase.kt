package com.uriolus.barometer.features.secondneedle.domain.usecases

import com.uriolus.barometer.features.secondneedle.domain.repository.SecondNeedleRepository

class SetSecondNeedleValueUseCase(private val repository: SecondNeedleRepository) {
    suspend fun exec(value: Float) = repository.setSecondNeedleValue(value)
}
