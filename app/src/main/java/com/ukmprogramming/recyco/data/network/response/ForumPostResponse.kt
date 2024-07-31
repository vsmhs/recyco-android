package com.ukmprogramming.recyco.data.network.response

import com.google.gson.annotations.SerializedName
import com.ukmprogramming.recyco.data.network.response.models.ForumPostWithReply

data class ForumPostResponse(
    @field:SerializedName("data")
    val data: ForumPostWithReply? = null,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)
