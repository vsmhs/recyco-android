package com.ukmprogramming.recyco.data

import com.ukmprogramming.recyco.data.network.service.APIService
import com.ukmprogramming.recyco.data.preferences.AppPreferences
import com.ukmprogramming.recyco.util.MarketTransactionStatuses
import com.ukmprogramming.recyco.util.formatBearerToken
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRepository @Inject constructor(
    private val appPreferences: AppPreferences,
    private val apiService: APIService
) {
    suspend fun getMarketItems() = apiService.getMarketItems(
        appPreferences.getToken().first().formatBearerToken()
    )

    suspend fun createMarketItem(
        name: String,
        price: Double,
        weight: Double,
        thumbnail: File,
        description: String?,
    ) = apiService.createMarketItem(
        appPreferences.getToken().first().formatBearerToken(),
        name.toRequestBody("text/plain".toMediaType()),
        price.toString().toRequestBody("text/plain".toMediaType()),
        weight.toString().toRequestBody("text/plain".toMediaType()),
        MultipartBody.Part.createFormData(
            "thumbnail",
            thumbnail.name,
            thumbnail.asRequestBody("image/jpg".toMediaType())
        ),
        description?.toRequestBody("text/plain".toMediaType()),
    )

    suspend fun getMarketItemById(itemId: String) = apiService.getMarketItemById(
        appPreferences.getToken().first().formatBearerToken(),
        itemId
    )

    suspend fun editMarketItem(
        itemId: String,
        name: String?,
        price: Double?,
        weight: Double?,
        description: String?,
        thumbnail: File?,
        status: String?
    ) = apiService.editMarketItem(
        appPreferences.getToken().first().formatBearerToken(),
        itemId,
        name?.toRequestBody("text/plain".toMediaType()),
        price?.toString()?.toRequestBody("text/plain".toMediaType()),
        weight?.toString()?.toRequestBody("text/plain".toMediaType()),
        description?.toRequestBody("text/plain".toMediaType()),
        thumbnail?.let {
            MultipartBody.Part.createFormData(
                "thumbnail",
                it.name,
                it.asRequestBody("image/jpg".toMediaType())
            )
        },
        status?.toRequestBody("text/plain".toMediaType())
    )

    suspend fun getSelfMarketItems() = apiService.getSelfMarketItems(
        appPreferences.getToken().first().formatBearerToken()
    )

    suspend fun getMarketTransactions() = apiService.getMarketTransactions(
        appPreferences.getToken().first().formatBearerToken()
    )

    suspend fun getMarketTransactionById(itemId: String) = apiService.getMarketTransactionByItemId(
        appPreferences.getToken().first().formatBearerToken(),
        itemId
    )

    suspend fun createMarketTransaction(
        itemId: String,
        recipientName: String?,
        recipientPhone: String?,
        description: String?,
        pickupLocationAddress: String?,
        pickupLocationDescription: String?
    ) = apiService.createMarketTransaction(
        appPreferences.getToken().first().formatBearerToken(),
        itemId,
        recipientName,
        recipientPhone,
        description,
        pickupLocationAddress,
        pickupLocationDescription
    )

    suspend fun updateMarketTransaction(
        itemId: String,
        status: MarketTransactionStatuses
    ) = apiService.editMarketTransaction(
        appPreferences.getToken().first().formatBearerToken(),
        itemId,
        status.name
    )
}