package com.ukmprogramming.recyco.data.network.response

import com.google.gson.annotations.SerializedName
import com.ukmprogramming.recyco.data.network.response.models.MarketTransactionItem

data class MarketTransactionResponse(
    @field:SerializedName("data")
    val data: MarketTransactionItem? = null,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)
