package com.uriolus.barometer.background.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.uriolus.barometer.background.database.dao.PressureDao
import com.uriolus.barometer.background.database.entity.PressureReading
import com.uriolus.barometer.background.datasource.BarometerDataSource
import kotlinx.coroutines.flow.first

class SavePressureWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val barometerDataSource: BarometerDataSource,
    private val pressureDao: PressureDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Start the sensor, get the first reading, then stop it.
            barometerDataSource.start()
            val reading = barometerDataSource.getBarometerReadings().first()
            barometerDataSource.stop()

            // Save the reading to the database.
            val pressureEntity = PressureReading(
                timestamp = reading.timestamp,
                pressureValue = reading.pressure
            )
            pressureDao.insert(pressureEntity)

            Result.success()
        } catch (e: Exception) {
            // If something goes wrong, mark the work as failed.
            Result.failure()
        }
    }
}
