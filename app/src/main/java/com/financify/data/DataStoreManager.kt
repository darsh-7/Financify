package com.financify.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val BIOMETRIC_ENABLED_KEY = booleanPreferencesKey("biometric_enabled")
    }

    val biometricEnabledFlow: Flow<Boolean?> = dataStore.data.map {
        it[BIOMETRIC_ENABLED_KEY]
    }

    suspend fun setBiometricEnabled(isEnabled: Boolean) {
        dataStore.edit {
            it[BIOMETRIC_ENABLED_KEY] = isEnabled
        }
    }
}