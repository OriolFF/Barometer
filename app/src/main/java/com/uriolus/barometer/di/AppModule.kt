package com.uriolus.barometer.di

import com.uriolus.barometer.features.historic.domain.usecases.GetAllPressureReadingsUseCase
import com.uriolus.barometer.features.historic.presentation.HistoricViewModel
import com.uriolus.barometer.features.realtime.data.datasource.BarometerDataSource
import com.uriolus.barometer.features.realtime.data.datasource.RealBarometerDataSource
import com.uriolus.barometer.features.realtime.data.datasource.SecondNeedleDataSource
import com.uriolus.barometer.features.realtime.data.repository.BarometerRepositoryImpl
import com.uriolus.barometer.features.realtime.data.repository.SecondNeedleRepositoryImpl
import com.uriolus.barometer.features.realtime.domain.repository.BarometerRepository
import com.uriolus.barometer.features.realtime.domain.repository.SecondNeedleRepository
import com.uriolus.barometer.features.realtime.domain.usecases.GetSecondNeedleValueUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.SetSecondNeedleValueUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.StartBarometerUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.StopBarometerUseCase
import com.uriolus.barometer.features.realtime.domain.usecases.SubscribeBarometerUseCase
import com.uriolus.barometer.features.realtime.presentation.BarometerViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf

import org.koin.dsl.module

val appModule = module {
    // Data
    single<BarometerDataSource> { RealBarometerDataSource(androidContext()) }
    single<BarometerRepository> { BarometerRepositoryImpl(get()) }
    single { SecondNeedleDataSource(androidContext()) }
    single<SecondNeedleRepository> { SecondNeedleRepositoryImpl(get()) }

    // Domain
    factory { StartBarometerUseCase(get()) }
    factory { StopBarometerUseCase(get()) }
    factory { SubscribeBarometerUseCase(get()) }
    factory { GetSecondNeedleValueUseCase(get()) }
    factory { SetSecondNeedleValueUseCase(get()) }
    factory { GetAllPressureReadingsUseCase(get()) }

    // Presentation
    viewModelOf(::BarometerViewModel)
    viewModelOf(::HistoricViewModel)
}
