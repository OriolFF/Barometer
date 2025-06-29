package com.uriolus.barometer

import android.app.Application
import com.uriolus.barometer.background.di.backgroundModule
import com.uriolus.barometer.background.worker.WorkScheduler
import com.uriolus.barometer.di.appModule
import com.uriolus.barometer.features.historic.di.historicModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class BarometerApp : Application() {

    private val workScheduler: WorkScheduler by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BarometerApp)
            modules(listOf(appModule, backgroundModule, historicModule))
            // Let Koin configure WorkManager
            workManagerFactory()
        }

        workScheduler.scheduleSensorWork(this)
    }
}
