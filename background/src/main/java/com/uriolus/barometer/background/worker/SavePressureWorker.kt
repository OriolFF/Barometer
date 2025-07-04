package com.uriolus.barometer.background.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.uriolus.barometer.background.data.datasource.BarometerDataSource
import com.uriolus.barometer.background.domain.usecase.SavePressureReadingUseCase
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SavePressureWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val barometerDataSource: BarometerDataSource by inject()
    private val savePressureReadingUseCase: SavePressureReadingUseCase by inject()

    override suspend fun doWork(): Result {
        return try {
            // Start the sensor, get the first reading, then stop it.
            barometerDataSource.start()
            val reading = barometerDataSource.getBarometerReadings().first()
            barometerDataSource.stop()

            // Save the reading using the use case
            savePressureReadingUseCase(reading)

            Result.success()
        } catch (e: Exception) {
            // If something goes wrong, mark the work as failed.
            Log.e("SavePressureWorker", "Error saving pressure reading", e)
            Result.failure()
        }
    }
}
