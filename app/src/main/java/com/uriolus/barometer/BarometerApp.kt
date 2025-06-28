package com.uriolus.barometer

import android.app.Application
import androidx.work.Configuration
import com.uriolus.barometer.di.KoinWorkerFactory
import com.uriolus.barometer.di.appModule
import com.uriolus.barometer.background.di.backgroundModule
import com.uriolus.barometer.background.worker.WorkScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BarometerApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BarometerApp)
            modules(listOf(appModule, backgroundModule))
        }
        WorkScheduler().scheduleSensorWork(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(KoinWorkerFactory())
            .build()
}
