package com.ukmprogramming.recyco.ui.activities.discussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.ForumRepository
import com.ukmprogramming.recyco.data.network.response.models.ForumPostWithReply
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscussionViewModel @Inject constructor(
    private val forumRepository: ForumRepository
) : ViewModel() {
    private val _addReplyState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val addReplyState: LiveData<ResultState<SingleEvent<String>>>
        get() = _addReplyState

    private val _dataState = MutableLiveData<ResultState<ForumPostWithReply>>()
    val dataState: LiveData<ResultState<ForumPostWithReply>>
        get() = _dataState

    fun addReply(
        postId: String,
        description: String
    ) = viewModelScope.launch {
        _addReplyState.value = ResultState.Loading

        try {
            val response = forumRepository.createForumPostReply(
                postId, description
            )

            if (response.success) {
                _addReplyState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _addReplyState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _addReplyState.value = ResultState.Error(SingleEvent(e))
        }
    }

    fun getForumPostWithReply(
        id: String
    ) = viewModelScope.launch {
        _dataState.value = ResultState.Loading

        try {
            val response = forumRepository.getForumPostById(id)

            if (response.success && response.data != null) {
                _dataState.value = ResultState.Success(response.data)
            } else {
                _dataState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _dataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}