package com.ukmprogramming.recyco.data.network.response

import com.google.gson.annotations.SerializedName
import com.ukmprogramming.recyco.data.network.response.models.MarketTransactionsItem

data class MarketTransactionsResponse(
	@field:SerializedName("data")
	val data: List<MarketTransactionsItem>? = null,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
