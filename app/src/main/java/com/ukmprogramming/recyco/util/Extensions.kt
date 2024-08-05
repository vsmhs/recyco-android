package com.ukmprogramming.recyco.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.ukmprogramming.recyco.data.network.response.BaseResponse
import de.hdodenhof.circleimageview.BuildConfig
import retrofit2.HttpException

val Context.dataStore by preferencesDataStore(Constants.PREFERENCES_NAME)

fun String?.formatBearerToken() = "Bearer $this"

fun Exception.handleHttpException(): String {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    }

    return if (this is HttpException) {
        this.response()?.errorBody()?.string()?.let {
            try {
                Gson().fromJson(it, BaseResponse::class.java).message
            } catch (_: Exception) {
                "Unexpected Error"
            }
        } ?: run {
            "Unexpected Error"
        }
    } else {
        "Unexpected Error"
    }
}