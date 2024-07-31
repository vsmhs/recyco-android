package com.ukmprogramming.recyco.data.network.response.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketTransactionStatusModel(
    @SerializedName("description")
    val description: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("status")
    val status: String
) : Parcelable