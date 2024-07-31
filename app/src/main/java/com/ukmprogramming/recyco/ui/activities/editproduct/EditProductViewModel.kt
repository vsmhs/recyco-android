package com.ukmprogramming.recyco.ui.activities.editproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.data.UserRepository
import com.ukmprogramming.recyco.data.network.response.models.MarketItem
import com.ukmprogramming.recyco.data.network.response.models.User
import com.ukmprogramming.recyco.util.MarketTransactionStatuses
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val marketRepository: MarketRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userDataState = MutableLiveData<ResultState<User>>()
    val userDataState: LiveData<ResultState<User>>
        get() = _userDataState

    private val _completeOrderState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val completeOrderState: LiveData<ResultState<SingleEvent<String>>>
        get() = _completeOrderState

    private val _updateOrderState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val updateOrderState: LiveData<ResultState<SingleEvent<String>>>
        get() = _updateOrderState

    private val _orderState = MutableLiveData<MarketTransactionStatuses?>(null)
    val orderState: LiveData<MarketTransactionStatuses?>
        get() = _orderState

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

    fun getOrderStatus(itemId: String) = viewModelScope.launch {
        try {
            val currentStatus = marketRepository.getMarketTransactionById(itemId)
                .data
                ?.allStatus
                ?.maxByOrNull {
                    it.createdAt
                }
                ?.status

            if (currentStatus == null) {
                throw Exception("Failed to get current status")
            }

            _orderState.value = when (currentStatus) {
                MarketTransactionStatuses.ON_PROCESS.name -> MarketTransactionStatuses.ON_PROCESS
                MarketTransactionStatuses.ON_DELIVER.name -> MarketTransactionStatuses.ON_DELIVER
                MarketTransactionStatuses.FINISHED.name -> MarketTransactionStatuses.FINISHED
                MarketTransactionStatuses.CANCELLED.name -> MarketTransactionStatuses.CANCELLED
                else -> {
                    throw Exception("Failed to get current status")
                }
            }
        } catch (_: Exception) {
        }
    }

    fun completeOrder(
        marketItem: MarketItem
    ) = viewModelScope.launch {
        _updateOrderState.value = ResultState.Loading

        try {
            val response = marketRepository.editMarketItem(
                marketItem.id,
                null,
                null,
                null,
                null,
                null,
                MarketTransactionStatuses.FINISHED.name
            )

            if (response.success) {
                _updateOrderState.value = ResultState.Success(SingleEvent(response.message))
                _orderState.value = MarketTransactionStatuses.FINISHED
            } else {
                _updateOrderState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _updateOrderState.value = ResultState.Error(SingleEvent(e))
        }
    }

    fun updateOrder(itemId: String) = viewModelScope.launch {
        _updateOrderState.value = ResultState.Loading

        try {
            val currentStatus = marketRepository.getMarketTransactionById(itemId)
                .data
                ?.allStatus
                ?.maxByOrNull {
                    it.createdAt
                }
                ?.status

            if (currentStatus == null) {
                throw Exception("Failed to update current status")
            }

            val response = marketRepository.updateMarketTransaction(
                itemId,
                when (currentStatus) {
                    MarketTransactionStatuses.ON_PROCESS.name -> MarketTransactionStatuses.ON_DELIVER
                    MarketTransactionStatuses.ON_DELIVER.name -> MarketTransactionStatuses.FINISHED
                    else -> {
                        throw Exception("Failed to update current status")
                    }
                }
            )

            if (response.success && response.data != null) {
                _updateOrderState.value = ResultState.Success(SingleEvent(response.message))
                _orderState.value = when (currentStatus) {
                    MarketTransactionStatuses.ON_PROCESS.name -> MarketTransactionStatuses.ON_DELIVER
                    MarketTransactionStatuses.ON_DELIVER.name -> MarketTransactionStatuses.FINISHED
                    MarketTransactionStatuses.FINISHED.name -> MarketTransactionStatuses.FINISHED
                    MarketTransactionStatuses.CANCELLED.name -> MarketTransactionStatuses.CANCELLED
                    else -> null
                }
            } else {
                _updateOrderState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _updateOrderState.value = ResultState.Error(SingleEvent(e))
        }
    }
}