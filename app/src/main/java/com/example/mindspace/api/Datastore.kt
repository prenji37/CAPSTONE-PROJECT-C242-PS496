package com.example.mindspace.api

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property for Context to access DataStore
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object Datastore {
    private val USER_LOGGED_IN = booleanPreferencesKey("user_logged_in")

    suspend fun saveUserLoggedIn(context: Context, isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN] = isLoggedIn
        }
    }

    fun isUserLoggedInFlow(context: Context): Flow<Boolean?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[USER_LOGGED_IN]
            }
    }
}
