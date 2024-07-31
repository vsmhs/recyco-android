package com.ukmprogramming.recyco.ui.activities.productorderhistorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.data.network.response.models.MarketTransactionsItem
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductOrderHistoryListViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {
    private val _orderHistoryDataState =
        MutableLiveData<ResultState<List<MarketTransactionsItem>>>()
    val orderHistoryDataState: LiveData<ResultState<List<MarketTransactionsItem>>>
        get() = _orderHistoryDataState

    fun getOrderHistory() = viewModelScope.launch {
        _orderHistoryDataState.value = ResultState.Loading

        try {
            val response = marketRepository.getMarketTransactions()

            if (response.success && response.data != null) {
                _orderHistoryDataState.value = ResultState.Success(response.data)
            } else {
                _orderHistoryDataState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _orderHistoryDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}