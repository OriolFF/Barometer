package com.uriolus.barometer.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.uriolus.barometer.background.database.dao.PressureDao
import com.uriolus.barometer.background.datasource.BarometerDataSource
import com.uriolus.barometer.background.worker.SavePressureWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinWorkerFactory : WorkerFactory(), KoinComponent {

    private val barometerDataSource: BarometerDataSource by inject()
    private val pressureDao: PressureDao by inject()

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SavePressureWorker::class.java.name -> {
                SavePressureWorker(
                    appContext,
                    workerParameters,
                    barometerDataSource,
                    pressureDao
                )
            }
            else -> {
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
            }
        }
    }
}
