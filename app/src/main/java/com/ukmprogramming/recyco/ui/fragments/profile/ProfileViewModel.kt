package com.ukmprogramming.recyco.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukmprogramming.recyco.data.AuthRepository
import com.ukmprogramming.recyco.data.UserRepository
import com.ukmprogramming.recyco.data.network.response.models.User
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _userDataState = MutableLiveData<ResultState<User>>()
    val userDataState: LiveData<ResultState<User>>
        get() = _userDataState

    private val _logoutState = MutableLiveData<ResultState<SingleEvent<String>>>()
    val logoutState: LiveData<ResultState<SingleEvent<String>>>
        get() = _logoutState

    fun logout() = viewModelScope.launch {
        _logoutState.value = ResultState.Loading

        try {
            val response = authRepository.logout()

            if (response.success) {
                authRepository.clearPreferences()
                _logoutState.value = ResultState.Success(SingleEvent(response.message))
            } else {
                _logoutState.value = ResultState.Error(SingleEvent(Exception(response.message)))
            }
        } catch (e: Exception) {
            _logoutState.value = ResultState.Error(SingleEvent(e))
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
}