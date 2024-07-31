package com.ukmprogramming.recyco.ui.activities.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.MarketRepository
import com.ukmprogramming.recyco.util.Helpers
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {
    private val _createItemState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val createItemState: LiveData<ResultState<SingleEvent<String>>>
        get() = _createItemState

    fun createMarketItem(
        name: String,
        price: Double,
        weight: Double,
        thumbnail: File,
        description: String?,
    ) = viewModelScope.launch {
        _createItemState.value = ResultState.Loading

        try {
            val response = marketRepository.createMarketItem(
                name, price, weight, Helpers.compressImage(thumbnail), description,
            )

            if (response.success) {
                _createItemState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _createItemState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _createItemState.value = ResultState.Error(SingleEvent(e))
        }
    }
}