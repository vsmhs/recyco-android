package com.ukmprogramming.recyco.data.network.response.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketItem(
    @field:SerializedName("price")
    val price: Double,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("posted_by")
    val postedBy: User,

    @field:SerializedName("weight")
    val weight: Double,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,

    @field:SerializedName("posted_at")
    val postedAt: String? = null
): Parcelable