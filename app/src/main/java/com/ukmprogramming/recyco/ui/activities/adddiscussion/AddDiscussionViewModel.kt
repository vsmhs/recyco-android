package com.ukmprogramming.recyco.ui.activities.adddiscussion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.ForumRepository
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiscussionViewModel @Inject constructor(
    private val forumRepository: ForumRepository
) : ViewModel() {
    private val _addDiscussionState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val addDiscussionState: LiveData<ResultState<SingleEvent<String>>>
        get() = _addDiscussionState

    fun addDiscussion(
        title: String,
        description: String?
    ) = viewModelScope.launch {
        _addDiscussionState.value = ResultState.Loading

        try {
            val response = forumRepository.createForumPost(
                title, description
            )

            if (response.success) {
                _addDiscussionState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _addDiscussionState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _addDiscussionState.value = ResultState.Error(SingleEvent(e))
        }
    }
}