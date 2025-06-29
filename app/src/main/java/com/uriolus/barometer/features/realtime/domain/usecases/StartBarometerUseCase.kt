package com.uriolus.barometer.features.realtime.domain.usecases

import com.uriolus.barometer.features.realtime.domain.repository.BarometerRepository

class StartBarometerUseCase(private val repository: BarometerRepository) {
    operator fun invoke() {
        repository.start()
    }
}
