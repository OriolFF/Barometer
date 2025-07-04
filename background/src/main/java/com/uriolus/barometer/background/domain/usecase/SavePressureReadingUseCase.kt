package com.uriolus.barometer.background.domain.usecase

import com.uriolus.barometer.background.domain.model.BarometerReading
import com.uriolus.barometer.background.domain.PressureRepository

class SavePressureReadingUseCase(
    private val pressureRepository: PressureRepository
) {
    suspend operator fun invoke(reading: BarometerReading) {
        pressureRepository.saveReading(reading)
    }
}
