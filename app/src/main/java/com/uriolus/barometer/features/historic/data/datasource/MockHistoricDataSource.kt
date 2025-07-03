package com.uriolus.barometer.features.historic.data.datasource

import com.uriolus.barometer.background.data.database.entity.PressureReading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.sin
import kotlin.random.Random

class MockHistoricDataSource : HistoricDataSource {
    override fun getAll(): Flow<List<PressureReading>> {
        val mockData = generateMockData()
        return flowOf(mockData)
    }

    private fun generateMockData(): List<PressureReading> {
        val readings = mutableListOf<PressureReading>()
        val baseTime = System.currentTimeMillis()
        
        // Base pressure around sea level (1013.25 hPa)
        val basePressure = 1013.25f
        
        // Generate 100 readings with a sine wave pattern plus some noise
        for (i in 0 until 100) {
            // Time goes backwards (newest first)
            val timestamp = baseTime - (i * 15 * 60 * 1000) // 15 minutes between readings
            
            // Create a sine wave with a 24-hour period (typical for daily pressure cycles)
            val hourOfDay = (timestamp / (60 * 60 * 1000)) % 24
            val sinComponent = sin(hourOfDay * Math.PI / 12).toFloat() * 3f // 3 hPa amplitude
            
            // Add some random noise (±0.5 hPa)
            val noise = Random.nextFloat() - 0.5f
            
            // Calculate the pressure value with a slight upward or downward trend
            val trendComponent = (i / 100f) * 5f * (if (Random.nextBoolean()) 1 else -1)
            val pressureValue = basePressure + sinComponent + noise + trendComponent
            
            readings.add(
                PressureReading(
                    id = i.toLong(),
                    timestamp = timestamp,
                    pressureValue = pressureValue
                )
            )
        }
        
        return readings // Return newest first (as expected by the repository)
    }
}
