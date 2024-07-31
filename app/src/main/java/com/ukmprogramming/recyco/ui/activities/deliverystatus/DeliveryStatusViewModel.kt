package com.ukmprogramming.recyco.ui.activities.deliverystatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.data.network.response.models.MarketTransactionItem
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryStatusViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {
    private val _marketTransactionDataState = MutableLiveData<ResultState<MarketTransactionItem>>()
    val marketTransactionDataState: LiveData<ResultState<MarketTransactionItem>>
        get() = _marketTransactionDataState

    fun getMarketTransactionById(itemId: String) = viewModelScope.launch {
        _marketTransactionDataState.value = ResultState.Loading

        try {
            val response = marketRepository.getMarketTransactionById(itemId)

            if (response.success && response.data != null) {
                _marketTransactionDataState.value = ResultState.Success(response.data)
            } else {
                _marketTransactionDataState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _marketTransactionDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}