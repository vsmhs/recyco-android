package com.ukmprogramming.recyco.data

import com.ukmprogramming.recyco.data.network.service.APIService
import com.ukmprogramming.recyco.data.preferences.AppPreferences
import com.ukmprogramming.recyco.util.formatBearerToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForumRepository @Inject constructor(
    private val apiService: APIService,
    private val appPreferences: AppPreferences
) {
    suspend fun getForumPosts() = apiService.getForumPosts(
        appPreferences.getToken().first().formatBearerToken()
    )

    suspend fun getForumPostById(id: String) = apiService.getForumPostById(
        appPreferences.getToken().first().formatBearerToken(),
        id
    )

    suspend fun createForumPost(
        title: String,
        description: String?,
    ) = apiService.createForumPost(
        appPreferences.getToken().first().formatBearerToken(),
        title,
        description
    )

    suspend fun createForumPostReply(
        postId: String,
        description: String,
    ) = apiService.createForumPostReply(
        appPreferences.getToken().first().formatBearerToken(),
        postId,
        description
    )
}