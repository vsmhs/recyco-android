package com.ukmprogramming.recyco.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ukmprogramming.recyco.databinding.FragmentProfileBinding
import com.ukmprogramming.recyco.ui.activities.login.LoginActivity
import com.ukmprogramming.recyco.ui.activities.reward.RewardActivity
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        binding.apply {
            btnCoins.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        RewardActivity::class.java
                    )
                )
            }

            btnLogout.setOnClickListener {
                viewModel.logout()
            }

            viewModel.userDataState.observe(viewLifecycleOwner) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    tvName.text = resultState.data.name
                    tvPhoneNumber.text = resultState.data.phoneNumber
                }
            }

            viewModel.logoutState.observe(viewLifecycleOwner) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    startActivity(
                        Intent(
                            activity,
                            LoginActivity::class.java
                        )
                    )
                    activity.finish()
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()
                        ?.let { message ->
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        viewModel.getUserProfile()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}