package com.ukmprogramming.recyco.ui.activities.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.TreatmentLocationRepository
import com.ukmprogramming.recyco.data.network.response.models.TreatmentLocation
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val treatmentLocationRepository: TreatmentLocationRepository
) : ViewModel() {
    private val _locationDataState = MutableLiveData<ResultState<List<TreatmentLocation>>>()
    val locationDataState: LiveData<ResultState<List<TreatmentLocation>>>
        get() = _locationDataState

    fun getForumPosts() = viewModelScope.launch {
        _locationDataState.value = ResultState.Loading

        try {
            val response = treatmentLocationRepository.getTreatmentLocations()

            if (response.success && response.data != null) {
                _locationDataState.value = ResultState.Success(response.data)
            } else {
                _locationDataState.value =
                    ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _locationDataState.value = ResultState.Error(SingleEvent(e))
        }
    }
}