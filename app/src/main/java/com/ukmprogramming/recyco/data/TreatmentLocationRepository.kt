package com.ukmprogramming.recyco.data

import com.ukmprogramming.recyco.data.network.service.APIService
import com.ukmprogramming.recyco.data.preferences.AppPreferences
import com.ukmprogramming.recyco.util.formatBearerToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TreatmentLocationRepository @Inject constructor(
    private val apiService: APIService,
    private val appPreferences: AppPreferences
) {
    suspend fun getTreatmentLocations() = apiService.getTreatmentLocations(
        appPreferences.getToken().first().formatBearerToken()
    )
}