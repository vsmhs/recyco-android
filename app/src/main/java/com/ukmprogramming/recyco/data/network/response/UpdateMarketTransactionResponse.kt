package com.ukmprogramming.recyco.data.network.response

import com.google.gson.annotations.SerializedName
import com.ukmprogramming.recyco.data.network.response.models.MarketTransactionStatusModel

data class UpdateMarketTransactionResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: MarketTransactionStatusModel?
)
