package com.ukmprogramming.recyco.data.network.response.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TreatmentLocation(
    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Int,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("lat")
    val lat: Int
) : Parcelable