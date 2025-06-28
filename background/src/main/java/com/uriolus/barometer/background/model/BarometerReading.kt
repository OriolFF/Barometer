package com.uriolus.barometer.background.model

data class BarometerReading(
    val pressure: Float,
    val timestamp: Long = System.currentTimeMillis()
)
