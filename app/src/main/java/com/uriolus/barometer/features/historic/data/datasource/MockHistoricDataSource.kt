package com.uriolus.barometer.features.historic.data.datasource

import com.uriolus.barometer.background.database.entity.PressureReading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import kotlin.random.Random

class MockHistoricDataSource : HistoricDataSource {
    override fun getAll(): Flow<List<PressureReading>> {
        val mockData = generateMockData()
        return flowOf(mockData)
    }

    private fun generateMockData(): List<PressureReading> {
        val readings = mutableListOf<PressureReading>()
        val calendar = Calendar.getInstance()

        for (i in 0 until 40) {
            // Go back one day at a time
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val timestamp = calendar.timeInMillis
            // Generate a realistic pressure value around a mean
            val pressure = 1013f + Random.nextFloat() * 20 - 10 // Fluctuation of +/- 10 hPa around 1013
            readings.add(
                PressureReading(
                    id = i.toLong(),
                    timestamp = timestamp,
                    pressureValue = pressure
                )
            )
        }
        return readings.reversed() // Return oldest first
    }
}
