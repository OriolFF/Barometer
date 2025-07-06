package com.uriolus.barometer.features.secondneedle.di

import com.uriolus.barometer.features.secondneedle.data.datasource.SecondNeedleLocalDataSource
import com.uriolus.barometer.features.secondneedle.data.repository.SecondNeedleRepositoryImpl
import com.uriolus.barometer.features.secondneedle.domain.repository.SecondNeedleRepository
import com.uriolus.barometer.features.secondneedle.domain.usecases.GetSecondNeedleValueUseCase
import com.uriolus.barometer.features.secondneedle.domain.usecases.SetSecondNeedleValueUseCase
import org.koin.dsl.module

val secondNeedleModule = module {
    single { SecondNeedleLocalDataSource(get()) }
    single<SecondNeedleRepository> { SecondNeedleRepositoryImpl(get()) }
    factory { GetSecondNeedleValueUseCase(get()) }
    factory { SetSecondNeedleValueUseCase(get()) }
}
