package com.ukmprogramming.recyco.ui.activities.ownedproductlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnedProductListViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {
    private val _marketDataState = MutableLiveData<ResultState<List<MarketItem>>>()
    val marketDataState: LiveData<ResultState<List<MarketItem>>>
        get() = _marketDataState

    fun getSelfMarketItems() = viewModelScope.launch {
        _marketDataState.value = ResultState.Loading

        try {
            val response = marketRepository.getSelfMarketItems()

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