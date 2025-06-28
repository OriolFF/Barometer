package com.uriolus.barometer.background.worker

/*
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier

class KoinWorkerFactory(private val koin: Koin) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
        return koin.get(
            workerClass,
            StringQualifier(workerClassName),
            { parametersOf(workerParameters) }
        ) as? ListenableWorker
    }
}
*/

/*
class KoinWorkerFactory : WorkerFactory(), KoinComponent {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
        return get(workerClass.kotlin, named(workerClass.name)) { workerParameters.let { org.koin.core.parameter.parametersOf(it) } }
    }
}
*/
