package com.ukmprogramming.recyco.ui.activities.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.AuthRepository
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import com.ukmprogramming.recyco.util.UserRoles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _registerState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val registerState: LiveData<ResultState<SingleEvent<String>>>
        get() = _registerState

    fun register(
        name: String,
        phoneNumber: String,
        password: String,
        role: UserRoles
    ) = viewModelScope.launch {
        _registerState.value = ResultState.Loading

        try {
            val response = authRepository.register(
                name, phoneNumber, password, role
            )

            if (response.success) {
                _registerState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _registerState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _registerState.value = ResultState.Error(SingleEvent(e))
        }
    }
}