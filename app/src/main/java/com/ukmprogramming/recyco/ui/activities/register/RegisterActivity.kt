package com.ukmprogramming.recyco.ui.activities.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ukmprogramming.recyco.databinding.ActivityRegisterBinding
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
                    "Produsen Skala Kecil" -> UserRoles.P_SMALL
                    "Produsen Skala Besar" -> UserRoles.P_LARGE
                    "Kolektor Skala Kecil" -> UserRoles.C_SMALL
                    "Kolektor Skala Besar" -> UserRoles.C_LARGE
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
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}