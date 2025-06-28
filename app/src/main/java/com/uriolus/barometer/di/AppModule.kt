package com.uriolus.barometer.di

import com.uriolus.barometer.data.datasource.BarometerDataSource
import com.uriolus.barometer.data.datasource.RealBarometerDataSource
import com.uriolus.barometer.data.repository.BarometerRepositoryImpl
import com.uriolus.barometer.domain.repository.BarometerRepository
import com.uriolus.barometer.domain.usecases.StartBarometerUseCase
import com.uriolus.barometer.domain.usecases.StopBarometerUseCase
import com.uriolus.barometer.domain.usecases.SubscribeBarometerUseCase
import com.uriolus.barometer.ui.BarometerViewModel
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
    viewModel { BarometerViewModel(get(), get(), get()) }
}
