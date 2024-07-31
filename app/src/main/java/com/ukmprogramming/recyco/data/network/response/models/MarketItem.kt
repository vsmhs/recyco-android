package com.ukmprogramming.recyco.data.network.response.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class MarketItem(
    @SerializedName("price")
    val price: Double,

    @SerializedName("name")
    val name: String,

    @SerializedName("posted_by")
    val postedBy: User,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("id")
    val id: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,

    @SerializedName("posted_at")
    val postedAt: Date?
) : Parcelable