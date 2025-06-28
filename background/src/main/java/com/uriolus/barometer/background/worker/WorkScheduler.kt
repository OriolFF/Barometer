package com.uriolus.barometer.background.worker

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkScheduler {

    fun scheduleSensorWork(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<SavePressureWorker>(
            30L,
            TimeUnit.MINUTES
        )
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "sensor_work",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}