package com.ukmprogramming.recyco.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ukmprogramming.recyco.databinding.FragmentMarketBinding
import com.ukmprogramming.recyco.databinding.FragmentProfileBinding
import com.ukmprogramming.recyco.ui.activities.login.LoginActivity
import com.ukmprogramming.recyco.util.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                // TODO rewards
            }

            btnLogout.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.logout()
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity.finish()
                }
            }

            viewModel.userDataState.observe(viewLifecycleOwner) { resultState ->
                if (resultState is ResultState.Success) {
                    tvName.text = resultState.data.name
                    tvPhoneNumber.text = resultState.data.phoneNumber
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