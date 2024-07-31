package com.ukmprogramming.recyco.ui.fragments.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.data.UserRepository
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.data.network.response.models.User
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val marketRepository: MarketRepository,
) : ViewModel() {
    private val _userDataState = MutableLiveData<ResultState<User>>()
    val userDataState: LiveData<ResultState<User>>
        get() = _userDataState

    private val _marketDataState = MutableLiveData<ResultState<List<MarketItem>>>()
    val marketDataState: LiveData<ResultState<List<MarketItem>>>
        get() = _marketDataState

    private val _loadingState = MediatorLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    init {
        _loadingState.addSource(_userDataState) {
            _loadingState.value = loadingState.value == true || it is ResultState.Loading
        }
        _loadingState.addSource(_marketDataState) {
            _loadingState.value = loadingState.value == true || it is ResultState.Loading
        }
    }

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

    fun getMarketItems() = viewModelScope.launch {
        _marketDataState.value = ResultState.Loading

        try {
            val response = marketRepository.getMarketItems()

            if (response.success && response.data != null) {
                _marketDataState.value = ResultState.Success(response.data)
            } else {
                _marketDataState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _marketDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}