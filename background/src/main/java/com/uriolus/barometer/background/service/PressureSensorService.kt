package com.uriolus.barometer.background.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.uriolus.barometer.background.R
import com.uriolus.barometer.background.data.datasource.BarometerDataSource
import com.uriolus.barometer.background.domain.usecase.SavePressureReadingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PressureSensorService : Service(), KoinComponent {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val barometerDataSource: BarometerDataSource by inject()
    private val savePressureReadingUseCase: SavePressureReadingUseCase by inject()

    companion object {
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "pressure_sensor_channel"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())

        scope.launch {
            try {
                barometerDataSource.start()
                val reading = barometerDataSource.getBarometerReadings().first()
                barometerDataSource.stop()
                Log.d("PressureSensorService", "Pressure reading: $reading")
                savePressureReadingUseCase(reading)
            } catch (e: Exception) {
                Log.e("PressureSensorService", "Error getting pressure reading", e)
            }
        }

        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Pressure Sensor Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Barometer")
            .setContentText("Reading pressure...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a proper icon
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
