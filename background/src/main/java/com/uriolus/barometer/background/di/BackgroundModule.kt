package com.uriolus.barometer.background.di

import androidx.room.Room
import com.uriolus.barometer.background.database.AppDatabase
import com.uriolus.barometer.background.datasource.BarometerDataSource
import com.uriolus.barometer.background.datasource.RealBarometerDataSource
import com.uriolus.barometer.background.worker.SavePressureWorker
import com.uriolus.barometer.background.worker.WorkScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val backgroundModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "barometer-db"
        ).build()
    }

    single {
        val database = get<AppDatabase>()
        database.pressureDao()
    }
    
    // Register the RealBarometerDataSource as the implementation for BarometerDataSource
    single<BarometerDataSource> {
        RealBarometerDataSource(androidContext())
    }
    
    // Register the WorkScheduler
    single { 
        WorkScheduler() 
    }
    
    // Register the SavePressureWorker for dependency injection
    workerOf(::SavePressureWorker)
}
