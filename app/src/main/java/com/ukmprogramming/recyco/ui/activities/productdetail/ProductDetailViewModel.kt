package com.ukmprogramming.recyco.ui.activities.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.UserRepository
import com.ukmprogramming.recyco.data.network.response.models.User
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _userDataState = MutableLiveData<ResultState<User>>()
    val userDataState: LiveData<ResultState<User>>
        get() = _userDataState

    fun getUserProfile() = viewModelScope.launch {
        _userDataState.value = ResultState.Loading

        try {
            val response = userRepository.getUserProfile()

            if (response.success && response.data != null) {
                _userDataState.value = ResultState.Success(response.data)
            } else {
                _userDataState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _userDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}