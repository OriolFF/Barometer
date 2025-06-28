package com.uriolus.barometer

import android.app.Application
import com.uriolus.barometer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BarometerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BarometerApp)
            modules(appModule)
        }
    }
}
