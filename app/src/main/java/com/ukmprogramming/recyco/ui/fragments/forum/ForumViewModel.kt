package com.ukmprogramming.recyco.ui.fragments.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.ForumRepository
import com.ukmprogramming.recyco.data.network.response.models.ForumPost
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val forumRepository: ForumRepository
) : ViewModel() {
    private val _forumDataState = MutableLiveData<ResultState<List<ForumPost>>>()
    val forumDataState: LiveData<ResultState<List<ForumPost>>>
        get() = _forumDataState

    fun getForumPosts() = viewModelScope.launch {
        _forumDataState.value = ResultState.Loading

        try {
            val response = forumRepository.getForumPosts()

            if (response.success && response.data != null) {
                _forumDataState.value = ResultState.Success(response.data)
            } else {
                _forumDataState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _forumDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}