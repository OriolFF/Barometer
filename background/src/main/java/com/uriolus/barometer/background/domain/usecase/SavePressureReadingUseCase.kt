package com.uriolus.barometer.background.domain.usecase

import com.uriolus.barometer.background.domain.PressureRepository
import com.uriolus.barometer.background.domain.model.BarometerReading

class SavePressureReadingUseCase(
    private val pressureRepository: PressureRepository
) {
    suspend operator fun invoke(reading: BarometerReading) {
        pressureRepository.saveReading(reading)
    }
}
