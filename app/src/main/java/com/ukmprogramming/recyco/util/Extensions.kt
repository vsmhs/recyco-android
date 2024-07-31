package com.ukmprogramming.recyco.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(Constants.PREFERENCES_NAME)

fun String?.formatBearerToken() = "Bearer $this"