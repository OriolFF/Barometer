package com.uriolus.barometer.features.realtime.data.datasource

import com.uriolus.barometer.features.realtime.domain.model.BarometerReading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MockBarometerDataSource : BarometerDataSource {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null
    private val barometerFlow = MutableSharedFlow<BarometerReading>()

    override fun getBarometerReadings(): Flow<BarometerReading> = barometerFlow

    override fun start() {
        job?.cancel()
        job = scope.launch {
            while (true) {
                val randomPressure = 980f + Random.nextFloat() * (1040f - 980f)
                barometerFlow.emit(BarometerReading(pressure = randomPressure))
                delay(1000)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}
