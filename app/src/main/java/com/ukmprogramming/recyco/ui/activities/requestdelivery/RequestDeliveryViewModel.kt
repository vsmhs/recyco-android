package com.ukmprogramming.recyco.ui.activities.requestdelivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestDeliveryViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {
    private val _requestDeliveryState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val requestDeliveryState: LiveData<ResultState<SingleEvent<String>>>
        get() = _requestDeliveryState

    fun requestDelivery(
        itemId: String,
        recipientName: String?,
        recipientPhone: String?,
        description: String?,
        pickupLocationAddress: String?,
        pickupLocationDescription: String?
    ) = viewModelScope.launch {
        _requestDeliveryState.value = ResultState.Loading

        try {
            val response = marketRepository.createMarketTransaction(
                itemId,
                recipientName,
                recipientPhone,
                description,
                pickupLocationAddress,
                pickupLocationDescription
            )

            if (response.success) {
                _requestDeliveryState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _requestDeliveryState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _requestDeliveryState.value = ResultState.Error(SingleEvent(e))
        }
    }
}