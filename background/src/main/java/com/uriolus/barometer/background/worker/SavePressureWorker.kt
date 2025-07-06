package com.uriolus.barometer.background.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.uriolus.barometer.background.service.PressureSensorService
import org.koin.core.component.KoinComponent

class SavePressureWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    override suspend fun doWork(): Result {
        return try {
            val intent = Intent(appContext, PressureSensorService::class.java)
            appContext.startService(intent)
            Result.success()
        } catch (e: Exception) {
            Log.e("SavePressureWorker", "Error starting service", e)
            Result.failure()
        }
    }
}
