package com.uriolus.barometer.background.domain.model

data class BarometerReading(
    val pressure: Float,
    val timestamp: Long = System.currentTimeMillis()
)
