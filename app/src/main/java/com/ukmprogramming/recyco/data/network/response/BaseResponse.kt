package com.ukmprogramming.recyco.data.network.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)
