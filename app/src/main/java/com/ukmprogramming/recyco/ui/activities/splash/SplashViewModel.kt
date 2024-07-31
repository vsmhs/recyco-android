package com.ukmprogramming.recyco.ui.activities.splash

import androidx.lifecycle.ViewModel
import com.ukmprogramming.recyco.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun isLoggedIn() = !authRepository.getToken().first().isNullOrEmpty()
}