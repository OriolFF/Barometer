package com.uriolus.barometer.domain.usecases

import com.uriolus.barometer.domain.repository.BarometerRepository

class StopBarometerUseCase(private val repository: BarometerRepository) {
    operator fun invoke() {
        repository.stop()
    }
}
