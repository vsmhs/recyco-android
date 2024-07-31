package com.ukmprogramming.recyco.data.network.response.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketTransactionItem(
    @field:SerializedName("item")
    val item: MarketItem,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("all_status")
    val allStatus: List<MarketTransactionStatusModel>
) : Parcelable