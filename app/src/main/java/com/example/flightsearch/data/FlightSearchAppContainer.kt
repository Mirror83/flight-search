package com.example.flightsearch.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

interface FlightSearchAppContainer {
    val flightSearchRepository: FlightSearchRepository
    val userPreferencesRepository: UserPreferencesRepository
}

private const val SEARCH_STRING_PREFERENCE_NAME = "search_string_preference"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SEARCH_STRING_PREFERENCE_NAME
)

class FlightSearchAppDataContainer(private val context: Context) : FlightSearchAppContainer {
    override val flightSearchRepository: FlightSearchRepository by lazy {
        val flightSearchDatabase = FlightSearchDatabase.getDatabase(context)
        OfflineFlightSearchRepository(
            flightSearchDatabase.getAirportDao(),
            flightSearchDatabase.getFavoriteDao()
        )
    }
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
}