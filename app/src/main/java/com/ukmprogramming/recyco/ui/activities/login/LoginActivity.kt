package com.ukmprogramming.recyco.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ukmprogramming.recyco.databinding.ActivityLoginBinding
import com.ukmprogramming.recyco.ui.activities.main.MainActivity
import com.ukmprogramming.recyco.ui.activities.register.RegisterActivity
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString()
                val password = etPassword.text.toString()

                viewModel.login(phoneNumber, password)
            }
            btnRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            viewModel.loginState.observe(this@LoginActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}