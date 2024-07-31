package com.ukmprogramming.recyco.data.network.service

import com.ukmprogramming.recyco.data.network.response.ArticlesResponse
import com.ukmprogramming.recyco.data.network.response.BaseResponse
import com.ukmprogramming.recyco.data.network.response.CommunitiesResponse
import com.ukmprogramming.recyco.data.network.response.ForumPostResponse
import com.ukmprogramming.recyco.data.network.response.ForumPostsResponse
import com.ukmprogramming.recyco.data.network.response.LoginResponse
import com.ukmprogramming.recyco.data.network.response.MarketItemResponse
import com.ukmprogramming.recyco.data.network.response.MarketItemsResponse
import com.ukmprogramming.recyco.data.network.response.MarketTransactionResponse
import com.ukmprogramming.recyco.data.network.response.MarketTransactionsResponse
import com.ukmprogramming.recyco.data.network.response.TreatmentLocationsResponse
import com.ukmprogramming.recyco.data.network.response.UpdateMarketTransactionResponse
import com.ukmprogramming.recyco.data.network.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface APIService {
    // AUTH
    @POST("/auth/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("phone_number") phoneNumber: String,
        @Field("password") password: String,
    ): LoginResponse

    @POST("/auth/logout")
    suspend fun logout(
        @Header("Authorization") bearerToken: String,
    ): BaseResponse

    @POST("/auth/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("phone_number") phoneNumber: String,
        @Field("password") password: String,
        @Field("role") role: String,
    ): UserResponse

    // USERS
    @GET("/user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") bearerToken: String,
    ): UserResponse

    // MARKETS
    @GET("/markets")
    suspend fun getMarketItems(
        @Header("Authorization") bearerToken: String,
    ): MarketItemsResponse

    @POST("/markets/")
    @Multipart
    suspend fun createMarketItem(
        @Header("Authorization") bearerToken: String,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part thumbnail: MultipartBody.Part,
        @Part("description") description: RequestBody?,
    ): BaseResponse

    @GET("/markets/{id}")
    suspend fun getMarketItemById(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
    ): MarketItemResponse

    @PUT("/markets/{id}")
    @Multipart
    suspend fun editMarketItem(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
        @Part("name") name: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("weight") weight: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part thumbnail: MultipartBody.Part?,
        @Part("status") status: RequestBody?
    ): BaseResponse

    @GET("/markets_self")
    suspend fun getSelfMarketItems(
        @Header("Authorization") bearerToken: String,
    ): MarketItemsResponse

    // MARKET TRANSACTIONS
    @GET("/market_transactions")
    suspend fun getMarketTransactions(
        @Header("Authorization") bearerToken: String,
    ): MarketTransactionsResponse

    @POST("/market_transactions")
    @FormUrlEncoded
    suspend fun createMarketTransaction(
        @Header("Authorization") bearerToken: String,
        @Field("item_id") itemId: String,
        @Field("recipient_name") recipientName: String?,
        @Field("recipient_phone") recipientPhone: String?,
        @Field("description") description: String?,
        @Field("pickup_location_address") pickupLocationAddress: String?,
        @Field("pickup_location_description") pickupLocationDescription: String?,
    ): BaseResponse

    @GET("/market_transactions/{id}")
    suspend fun getMarketTransactionByItemId(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
    ): MarketTransactionResponse

    @PUT("/market_transactions/{id}")
    @FormUrlEncoded
    suspend fun editMarketTransaction(
        @Header("Authorization") bearerToken: String,
        @Path("id") itemId: String,
        @Field("status") status: String,
    ): UpdateMarketTransactionResponse

    // ARTICLES
    @GET("/articles")
    suspend fun getArticles(
        @Header("Authorization") bearerToken: String,
    ): ArticlesResponse

    // COMMUNITIES
    @GET("/communities")
    suspend fun getCommunities(
        @Header("Authorization") bearerToken: String,
    ): CommunitiesResponse

    // FORUM POSTS
    @GET("/forum_posts")
    suspend fun getForumPosts(
        @Header("Authorization") bearerToken: String,
    ): ForumPostsResponse

    @POST("/forum_posts")
    @FormUrlEncoded
    suspend fun createForumPost(
        @Header("Authorization") bearerToken: String,
        @Field("title") title: String,
        @Field("description") description: String?,
    ): BaseResponse

    @GET("/forum_posts/{id}")
    suspend fun getForumPostById(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
    ): ForumPostResponse

    @POST("/forum_posts/{post_id}/replies")
    @FormUrlEncoded
    suspend fun createForumPostReply(
        @Header("Authorization") bearerToken: String,
        @Path("post_id") postId: String,
        @Field("description") description: String,
    ): BaseResponse

    // TREATMENT LOCATIONS
    @GET("/treatment_locations")
    suspend fun getTreatmentLocations(
        @Header("Authorization") bearerToken: String,
    ): TreatmentLocationsResponse
}