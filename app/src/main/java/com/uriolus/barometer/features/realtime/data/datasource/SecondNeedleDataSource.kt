package com.uriolus.barometer.features.realtime.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "second_needle_datastore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class SecondNeedleDataSource(private val context: Context) {
    private val secondNeedleKey = floatPreferencesKey("second_needle_value")

    val secondNeedleValue: Flow<Float?> = context.dataStore.data
        .map {
            it[secondNeedleKey]
        }

    suspend fun setSecondNeedleValue(value: Float) {
        context.dataStore.edit {
            it[secondNeedleKey] = value
        }
    }
}
