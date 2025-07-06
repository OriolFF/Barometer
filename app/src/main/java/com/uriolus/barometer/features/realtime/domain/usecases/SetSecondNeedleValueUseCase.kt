package com.uriolus.barometer.features.realtime.domain.usecases

import com.uriolus.barometer.features.realtime.domain.repository.SecondNeedleRepository

class SetSecondNeedleValueUseCase(private val repository: SecondNeedleRepository) {
    suspend fun exec(value: Float) = repository.setSecondNeedleValue(value)
}
