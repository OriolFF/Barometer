package com.uriolus.barometer.features.historic.di

import com.uriolus.barometer.features.historic.data.datasource.HistoricDataSource
import com.uriolus.barometer.features.historic.data.datasource.MockHistoricDataSource
import com.uriolus.barometer.features.historic.data.datasource.RoomHistoricDataSource
import com.uriolus.barometer.features.historic.data.repository.HistoricRepositoryImpl
import com.uriolus.barometer.features.historic.domain.repository.HistoricRepository
import com.uriolus.barometer.features.historic.domain.usecases.GetAllPressureReadingsUseCase
import com.uriolus.barometer.features.historic.presentation.HistoricViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val historicModule = module {
    // Data source
    single<HistoricDataSource> { RoomHistoricDataSource(get()) }
    single<HistoricDataSource> { MockHistoricDataSource() }

    // Repository
    single<HistoricRepository> { HistoricRepositoryImpl(get()) }

    // Use case
    factory { GetAllPressureReadingsUseCase(get()) }

    // ViewModel
    viewModel { HistoricViewModel(get()) }
}
