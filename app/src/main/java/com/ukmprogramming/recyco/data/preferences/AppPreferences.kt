package com.ukmprogramming.recyco.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class AppPreferences(private val dataStore: DataStore<Preferences>) {
    private val tokenKey = stringPreferencesKey("token_key")

    suspend fun setToken(token: String) = dataStore.edit {
        it[tokenKey] = token
    }

    fun getToken() = dataStore.data.map {
        it[tokenKey]
    }

    suspend fun clearPreferences() = dataStore.edit {
        it.clear()
    }
}