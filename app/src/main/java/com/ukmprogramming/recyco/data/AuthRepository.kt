package com.ukmprogramming.recyco.data

import com.ukmprogramming.recyco.data.network.service.APIService
import com.ukmprogramming.recyco.data.preferences.AppPreferences
import com.ukmprogramming.recyco.util.UserRoles
import com.ukmprogramming.recyco.util.formatBearerToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val appPreferences: AppPreferences,
    private val apiService: APIService
) {
    suspend fun login(
        phoneNumber: String,
        password: String,
    ) = apiService.login(
        phoneNumber, password
    )

    suspend fun register(
        name: String,
        phoneNumber: String,
        password: String,
        role: UserRoles
    ) = apiService.register(
        name,
        phoneNumber,
        password,
        role.name
    )

    suspend fun logout() = apiService.logout(
        appPreferences.getToken().first().formatBearerToken()
    )

    fun getToken() = appPreferences.getToken()

    suspend fun setToken(token: String) = appPreferences.setToken(token)

    suspend fun clearPreferences() = appPreferences.clearPreferences()
}