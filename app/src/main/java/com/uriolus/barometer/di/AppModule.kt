package com.uriolus.barometer.di

import com.uriolus.barometer.features.historic.presentation.HistoricViewModel
import com.uriolus.barometer.features.realtime.data.datasource.BarometerDataSource
import com.uriolus.barometer.features.realtime.data.datasource.RealBarometerDataSource
import com.uriolus.barometer.features.realtime.data.repository.BarometerRepositoryImpl
import com.uriolus.barometer.features.realtime.domain.repository.BarometerRepository
import com.uriolus.barometer.features.realtime.domain.usecases.StartBarometerUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.StopBarometerUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.SubscribeBarometerUseCase
import com.uriolus.barometer.features.realtime.presentation.BarometerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Data
    single<BarometerDataSource> { RealBarometerDataSource(androidContext()) }
    single<BarometerRepository> { BarometerRepositoryImpl(get()) }

    // Domain
    factory { StartBarometerUseCase(get()) }
    factory { StopBarometerUseCase(get()) }
    factory { SubscribeBarometerUseCase(get()) }

    // Presentation
    viewModel { BarometerViewModel(get(), get(), get(), get()) }

}
