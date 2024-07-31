package com.ukmprogramming.recyco.ui.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.AuthRepository
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val loginState: LiveData<ResultState<SingleEvent<String>>>
        get() = _loginState

    fun login(
        phoneNumber: String,
        password: String
    ) = viewModelScope.launch {
        _loginState.value = ResultState.Loading

        try {
            val response = authRepository.login(phoneNumber, password)

            if (response.success && response.data != null) {
                authRepository.setToken(response.data.token)
                _loginState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _loginState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _loginState.value = ResultState.Error(SingleEvent(e))
        }
    }
}