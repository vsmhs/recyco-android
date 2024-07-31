package com.ukmprogramming.recyco.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.ukmprogramming.recyco.data.network.response.BaseResponse
import retrofit2.HttpException

val Context.dataStore by preferencesDataStore(Constants.PREFERENCES_NAME)

fun String?.formatBearerToken() = "Bearer $this"

fun Exception.handleHttpException(context: Context) = if (this is HttpException) {
    this.response()?.errorBody()?.string()?.let {
        Gson().fromJson(it, BaseResponse::class.java).message
    } ?: run {
        "Unexpected Error"
    }
} else {
    "Unexpected Error"
}