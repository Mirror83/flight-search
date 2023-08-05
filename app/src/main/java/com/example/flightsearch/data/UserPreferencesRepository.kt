package com.example.flightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val searchString: Flow<String> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                Log.e(TAG, "Error reading preferences.", error)
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences ->
            preferences[SEARCH_STRING] ?: ""
        }

    private companion object {
        val SEARCH_STRING = stringPreferencesKey("search_string")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveUserSearchString(searchString: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[SEARCH_STRING] = searchString
            Log.d(TAG, preferences[SEARCH_STRING]!!)
        }
    }
}