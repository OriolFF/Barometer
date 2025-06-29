package com.uriolus.barometer.features.historic.domain.model

data class PressureSample(
    val timestamp: Long,
    val pressureValue: Float
)
