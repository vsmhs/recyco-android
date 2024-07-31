package com.ukmprogramming.recyco.data

import com.ukmprogramming.recyco.data.network.service.APIService
import com.ukmprogramming.recyco.data.preferences.AppPreferences
import com.ukmprogramming.recyco.util.formatBearerToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val appPreferences: AppPreferences,
    private val apiService: APIService
) {
    suspend fun getUserProfile() = apiService.getUserProfile(
        appPreferences.getToken().first().formatBearerToken()
    )
}