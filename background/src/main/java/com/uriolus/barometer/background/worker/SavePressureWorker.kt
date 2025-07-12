package com.uriolus.barometer.background.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.uriolus.barometer.background.R
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

    companion object {
        const val NOTIFICATION_ID = 2
        const val NOTIFICATION_CHANNEL_ID = "pressure_sensor_worker_channel"
    }

    override suspend fun doWork(): Result {
        val notification = createNotification()
        val foregroundInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
        setForeground(foregroundInfo)

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

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Pressure Sensor Worker",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Barometer")
            .setContentText("Reading pressure in background...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a proper icon
            .build()
    }
}
