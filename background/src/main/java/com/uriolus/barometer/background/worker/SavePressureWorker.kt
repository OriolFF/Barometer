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
            Log.d("SavePressureWorker", "Worker started")
            barometerDataSource.start()
            val reading = barometerDataSource.getBarometerReadings().first()
            barometerDataSource.stop()
            Log.d("SavePressureWorker", "Pressure reading: $reading")
            savePressureReadingUseCase(reading)
            Log.d("SavePressureWorker", "Worker finished successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("SavePressureWorker", "Error executing worker", e)
            try {
                barometerDataSource.stop()
            } catch (stopException: Exception) {
                Log.e("SavePressureWorker", "Error stopping data source", stopException)
            }
            Result.failure()
        }
    }
}
