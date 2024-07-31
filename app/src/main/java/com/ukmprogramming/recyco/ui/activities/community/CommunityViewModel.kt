package com.ukmprogramming.recyco.ui.activities.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.CommunityRepository
import com.ukmprogramming.recyco.data.network.response.models.Community
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    private val _communityDataState = MutableLiveData<ResultState<List<Community>>>()
    val communityDataState: LiveData<ResultState<List<Community>>>
        get() = _communityDataState

    fun getCommunities() = viewModelScope.launch {
        _communityDataState.value = ResultState.Loading

        try {
            val response = communityRepository.getCommunities()

            if (response.success && response.data != null) {
                _communityDataState.value = ResultState.Success(response.data)
            } else {
                _communityDataState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _communityDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}