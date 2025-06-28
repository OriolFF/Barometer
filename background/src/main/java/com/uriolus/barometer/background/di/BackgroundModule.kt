package com.uriolus.barometer.background.di

import androidx.room.Room
import com.uriolus.barometer.background.database.AppDatabase
import org.koin.android.ext.koin.androidContext
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
}
