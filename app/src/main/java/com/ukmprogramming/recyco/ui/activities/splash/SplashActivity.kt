package com.ukmprogramming.recyco.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ukmprogramming.recyco.databinding.ActivitySplashBinding
import com.ukmprogramming.recyco.ui.activities.login.LoginActivity
import com.ukmprogramming.recyco.ui.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(3000)
            startActivity(
                Intent(
                    this@SplashActivity,
                    if (viewModel.isLoggedIn()) {
                        MainActivity::class.java
                    } else {
                        LoginActivity::class.java
                    }
                )
            )
            finish()
        }
    }
}