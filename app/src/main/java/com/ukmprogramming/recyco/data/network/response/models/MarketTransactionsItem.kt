package com.ukmprogramming.recyco.data.network.response.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketTransactionsItem(
    @SerializedName("item")
	val item: MarketItem,

    @SerializedName("last_status")
	val lastStatus: MarketTransactionStatusModel,

    @SerializedName("id")
	val id: String
): Parcelable