package com.ralphmarondev.dragonfly.core.data.local.preferences

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(
    context: Context
) {
    private val appContext = context.applicationContext
    private val dataStore = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler {
            emptyPreferences()
        },
        produceFile = {
            appContext.preferencesDataStoreFile(DATASTORE_NAME)
        }
    )

    companion object {
        private const val DATASTORE_NAME = "dragonfly_preferences"

        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val AUTHENTICATED = booleanPreferencesKey("is_authenticated")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val VEHICLE_ID = stringPreferencesKey("vehicle_id")
        val LOCATION = stringPreferencesKey("location")
    }

    suspend fun setDarkMode(value: Boolean) {
        dataStore.edit { it[DARK_MODE] = value }
    }

    fun isDarkMode(): Flow<Boolean> {
        return dataStore.data.map { it[DARK_MODE] == true }
    }

    suspend fun setAuthenticated(value: Boolean) {
        dataStore.edit { it[AUTHENTICATED] = value }
    }

    fun isAuthenticated(): Flow<Boolean> {
        return dataStore.data.map { it[AUTHENTICATED] ?: false }
    }

    suspend fun setEmail(value: String) {
        dataStore.edit { it[EMAIL] = value }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { it[EMAIL] ?: "" }
    }

    suspend fun setPassword(value: String) {
        dataStore.edit { it[PASSWORD] = value }
    }

    fun getPassword(): Flow<String> {
        return dataStore.data.map { it[PASSWORD] ?: "" }
    }

    suspend fun setVehicleId(value: String) {
        dataStore.edit { it[VEHICLE_ID] = value }
    }

    fun getVehicleId(): Flow<String> {
        return dataStore.data.map { it[VEHICLE_ID] ?: "" }
    }

    suspend fun setLocation(lat: Double, lng: Double) {
        val value = "$lat,$lng"
        dataStore.edit { it[LOCATION] = value }
    }

    fun getLocation(): Flow<Pair<Double, Double>?> {
        return dataStore.data.map { prefs ->
            prefs[LOCATION]?.let {
                val parts = it.split(",")
                Pair(parts[0].toDouble(), parts[1].toDouble())
            }
        }
    }
}