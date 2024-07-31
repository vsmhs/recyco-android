package com.ukmprogramming.recyco.ui.activities.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityRegisterBinding
import com.ukmprogramming.recyco.ui.activities.login.LoginActivity
import com.ukmprogramming.recyco.ui.activities.main.MainActivity
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.UserRoles
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                finish()
            }

            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()
                val password = etPassword.text.toString()
                val role = when (etRole.text.toString()) {
                    UserRoles.P_SMALL.name -> UserRoles.P_SMALL
                    UserRoles.P_LARGE.name -> UserRoles.P_LARGE
                    UserRoles.C_SMALL.name -> UserRoles.C_SMALL
                    UserRoles.C_LARGE.name -> UserRoles.C_LARGE
                    else -> {
                        return@setOnClickListener
                    }
                }

                viewModel.register(name, phoneNumber, password, role)
            }

            viewModel.registerState.observe(this@RegisterActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException(this@RegisterActivity)
                        ?.let { message ->
                            Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }
}