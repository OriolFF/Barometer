package com.uriolus.barometer.background.di

import androidx.room.Room
import com.uriolus.barometer.background.data.database.AppDatabase
import com.uriolus.barometer.background.data.database.dao.PressureDao
import com.uriolus.barometer.background.data.datasource.BarometerDataSource
import com.uriolus.barometer.background.data.datasource.RealBarometerDataSource
import com.uriolus.barometer.background.domain.PressureRepository
import com.uriolus.barometer.background.data.repository.RealPressureRepository
import com.uriolus.barometer.background.domain.usecase.SavePressureReadingUseCase
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

    single<PressureDao> {
        get<AppDatabase>().pressureDao()
    }
    
    // Register the RealBarometerDataSource as the implementation for BarometerDataSource
    single<BarometerDataSource> {
        RealBarometerDataSource(androidContext())
    }
    
    // Register the Repository
    single<PressureRepository> {
        RealPressureRepository(get())
    }
    
    // Register the Use Case
    single {
        SavePressureReadingUseCase(get())
    }
    
    // Register the WorkScheduler
    single { 
        WorkScheduler() 
    }
    
    // Register the SavePressureWorker for dependency injection
    workerOf(::SavePressureWorker)
}
