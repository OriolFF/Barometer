package com.uriolus.barometer

import android.app.Application
import com.uriolus.barometer.di.appModule
import com.uriolus.barometer.background.di.backgroundModule
import com.uriolus.barometer.background.worker.WorkScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BarometerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BarometerApp)
            modules(listOf(appModule, backgroundModule))
        }
        WorkScheduler().scheduleSensorWork(this)
    }
}
